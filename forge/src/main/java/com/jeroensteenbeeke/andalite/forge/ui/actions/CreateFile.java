/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jeroensteenbeeke.andalite.forge.ui.actions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.andalite.core.ActionResult;

/**
 * Basic compoundable action implementation used to create various files
 * 
 * @author Jeroen Steenbeeke
 *
 */
public final class CreateFile extends AbstractCompoundableAction {
	/**
	 * Builder for the creation of XML files
	 * 
	 * @author Jeroen Steenbeeke
	 */
	public static class CreateXMLBuilder {
		private final File file;

		private final Map<String, String> namespaces;

		private final Map<String, String> xsds;

		private String defaultNamespaceUrl = null;

		/**
		 * Create a new builder for creating an XML file
		 * 
		 * @param file
		 *            The target file
		 */
		private CreateXMLBuilder(@Nonnull File file) {
			this.file = file;
			this.namespaces = new HashMap<>();
			this.xsds = new HashMap<>();
		}

		/**
		 * Set the default namespace of the XML document to given URL, and
		 * optionally bind an XSD to the url
		 * 
		 * @param url
		 *            The URL of the namespace
		 * @param xsd
		 *            The XSD tied to this namespace (optional)
		 * @return This builder
		 */
		public CreateXMLBuilder withDefaultNamespace(@Nonnull String url,
				@Nullable String xsd) {
			this.defaultNamespaceUrl = url;
			if (xsd != null) {
				xsds.put(url, xsd);
			}
			return this;
		}

		/**
		 * Add an XML namespace to the document, with the given prefix and
		 * optional XSD
		 * 
		 * @param prefix
		 *            The prefix to use in the XML document
		 * @param url
		 *            The namespace URL tied to the prefix
		 * @param xsd
		 *            The XSD tied to this URL (optional)
		 * @return This builder
		 */
		public CreateXMLBuilder withXmlNameSpace(@Nonnull String prefix,
				@Nonnull String url, @Nullable String xsd) {
			this.namespaces.put(prefix, url);

			if (xsd != null) {
				xsds.put(url, xsd);
			}

			return this;
		}

		/**
		 * Sets the root element of the document and creates the action
		 * 
		 * @param element
		 *            The root element of the document
		 * @return A CreateFile action that will create an XML document
		 */
		public CreateFile withRootElement(@Nonnull String element) {
			StringBuilder builder = new StringBuilder();
			builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

			builder.append("<").append(element);

			if (defaultNamespaceUrl != null) {
				builder.append(" xmlns=\"");
				builder.append(defaultNamespaceUrl);
				builder.append("\"");
			}

			if (!namespaces.isEmpty()) {
				namespaces.forEach((prefix, url) -> {
					builder.append("\n\t");
					builder.append("xmlns:");
					builder.append(prefix);
					builder.append("=\"");
					builder.append(url);
					builder.append("\"");
				});
			}

			if (!xsds.isEmpty()) {
				builder.append(
						"\n\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
				builder.append("\n\txsi:schemaLocation=\"");
				int c = 0;
				for (Entry<String, String> entry : xsds.entrySet()) {
					builder.append(entry.getKey());
					builder.append("\n\t\t");
					builder.append(entry.getValue());
					if (++c < xsds.size()) {
						builder.append("\n\t\t");
					}
				}

				builder.append("\"");
			}
			builder.append(">\n\n");
			builder.append("</").append(element).append(">\n");

			return new CreateFile(file).withInitialContents(builder.toString());
		}

	}

	private static final Logger logger = LoggerFactory
			.getLogger(CreateFile.class);

	private final File file;

	private String initialContents = null;

	/**
	 * Create a new action that will create a file
	 * 
	 * @param file
	 *            The target file
	 */
	public CreateFile(File file) {
		this.file = file;
		logger.info("Create {}", file.getAbsolutePath());
	}

	/**
	 * Sets the file's initial contents to the given String
	 * 
	 * @param contents
	 *            The contents to put in the file
	 * @return This action
	 */
	public CreateFile withInitialContents(String contents) {
		this.initialContents = contents;
		return this;
	}

	@Override
	public ActionResult perform() {
		if (file.exists()) {
			// If the file already exists, assume everything is fine
			return ActionResult.ok();
		} else {
			try {
				// Check if the target file's folder exists. We would get an
				// error if it doesn't exist
				if (!file.getParentFile().exists()) {
					// If it doesn't, try to create it
					if (!file.getParentFile().mkdirs()) {
						// If it can't be created, the action should fail
						return ActionResult.error(
								"Parent directory %s does not exist and could not create",
								file.getParentFile().getAbsolutePath());
					}
				}

				// Try to create the file
				if (file.createNewFile()) {
					// If we have initial contents. write them to file
					if (initialContents != null) {
						PrintWriter pw = new PrintWriter(file);
						pw.print(initialContents);
						pw.flush();
						pw.close();
					}

					return ActionResult.ok();
				} else {
					return ActionResult.error("Could not create file %s",
							file.getName());
				}
			} catch (IOException e) {
				return ActionResult.error(e.getMessage());
			}
		}

	}

	/**
	 * Convenience method to quickly create an empty Java class file
	 * 
	 * @param file
	 *            The target file
	 * @param packageName
	 *            The package to place the file in. This should not be the
	 *            default (empty) package. It is the developer's responsibility
	 *            to ensure the class is placed in the proper folder, and this
	 *            action does not check against it
	 * @param className
	 *            The name of the class to create
	 * @return A CreateFile action that will create an empty Java class source
	 *         file
	 */
	public static CreateFile emptyJavaClassFile(@Nonnull File file,
			@Nonnull String packageName, @Nonnull String className) {

		final StringBuilder initial = new StringBuilder();
		initial.append("package ").append(packageName).append(";\n\n");
		initial.append("public class ").append(className).append(" {\n\n");
		initial.append("}\n");

		return new CreateFile(file).withInitialContents(initial.toString());
	}

	/**
	 * Convenience method to quickly create an empty Java class file with
	 * default scope
	 * 
	 * @param file
	 *            The target file
	 * @param packageName
	 *            The package to place the file in. This should not be the
	 *            default (empty) package. It is the developer's responsibility
	 *            to ensure the class is placed in the proper folder, and this
	 *            action does not check against it
	 * @param className
	 *            The name of the class to create
	 * @return A CreateFile action that will create an empty Java class source
	 *         file
	 */
	public static CreateFile emptyNonPublicJavaClassFile(@Nonnull File file,
			@Nonnull String packageName, @Nonnull String className) {

		final StringBuilder initial = new StringBuilder();
		initial.append("package ").append(packageName).append(";\n\n");
		initial.append("class ").append(className).append(" {\n\n");
		initial.append("}\n");

		return new CreateFile(file).withInitialContents(initial.toString());
	}

	/**
	 * Convenience method to quickly create an empty Java interface file
	 * 
	 * @param file
	 *            The target file
	 * @param packageName
	 *            The package to place the file in. This should not be the
	 *            default (empty) package. It is the developer's responsibility
	 *            to ensure the class is placed in the proper folder, and this
	 *            action does not check against it
	 * @param className
	 *            The name of the interface to create
	 * @return A CreateFile action that will create an empty Java interface file
	 */
	public static CreateFile emptyJavaInterfaceFile(File file,
			String packageName, String className) {

		final StringBuilder initial = new StringBuilder();
		initial.append("package ").append(packageName).append(";\n\n");
		initial.append("public interface ").append(className).append(" {\n\n");
		initial.append("}\n");

		return new CreateFile(file).withInitialContents(initial.toString());
	}

	/**
	 * Convenience method to create an XML file builder
	 * 
	 * @param file
	 *            The target file
	 * @return A builder to further fill the XML file with namespaces and XSD
	 *         references
	 */
	public static CreateXMLBuilder emptyXmlFile(@Nonnull File file) {
		return new CreateXMLBuilder(file);
	}

	/**
	 * Convenience method to create an empty HTML file
	 * 
	 * @param file
	 *            The target file
	 * @param options
	 *            Options for elements that need to be added to the HTML file
	 * @return A CreateFile action that will create an empty HTML file
	 */
	public static CreateFile emptyHtmlFile(@Nonnull File file,
			@Nonnull IHTMLFileOption... options) {
		return emptyHtmlFile(file, null, options);
	}

	/**
	 * Convenience method to create an empty HTML file with specified XML
	 * namespaces
	 * 
	 * @param file
	 *            The target file
	 * @param xmlns
	 *            A Map of XML namespaces (prefixes as key, namespace URL as
	 *            value)
	 * @param options
	 *            Options for elements that need to be added to the HTML file
	 * @return A CreateFile action that will create an empty HTML file
	 */
	public static CreateFile emptyHtmlFile(@Nonnull File file,
			@Nullable Map<String, String> xmlns,
			@Nonnull IHTMLFileOption... options) {

		final StringBuilder initial = new StringBuilder();
		initial.append("<html");
		if (xmlns != null) {
			initial.append(" ");
			xmlns.forEach((prefix, url) -> {
				initial.append("xmlns:");
				initial.append(prefix);
				if (url != null) {
					initial.append("=\"");
					initial.append(url);
					initial.append("\"");
				}
			});
		}

		initial.append(">\n");
		Arrays.stream(options).forEach(o -> o.accept(initial));
		initial.append("</html>");
		return new CreateFile(file).withInitialContents(initial.toString());
	}

	/**
	 * Functional interface for adding elements to an empty HTML file
	 * 
	 * @author Jeroen Steenbeeke
	 *
	 */
	@FunctionalInterface
	public static interface IHTMLFileOption extends Consumer<StringBuilder> {

	}

	/**
	 * Default elements to add to empty HTML files
	 * 
	 * @author Jeroen Steenbeeke
	 *
	 */
	public enum DefaultHTML implements IHTMLFileOption {
		/**
		 * Adds a {@code <head></head>} block to the document
		 */
		HEAD {
			@Override
			public void accept(StringBuilder target) {
				target.append("\t<head>\n");
				target.append("\t</head>\n");
			}
		},
		/**
		 * Adds a {@code <body></body>} block to the document
		 */
		BODY {
			@Override
			public void accept(StringBuilder target) {
				target.append("\t<body>\n");
				target.append("\t</body>\n");
			}
		};
	}

}
