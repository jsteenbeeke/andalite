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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.hyperion.util.ActionResult;

public final class CreateFile extends AbstractCompoundableAction {

	public static class CreateXMLBuilder {
		private final File file;

		private final Map<String, String> namespaces;

		private final Map<String, String> xsds;

		private String defaultNamespaceUrl = null;

		public CreateXMLBuilder(@Nonnull File file) {
			this.file = file;
			this.namespaces = new HashMap<>();
			this.xsds = new HashMap<>();
		}

		public CreateXMLBuilder withDefaultNamespace(@Nonnull String url,
				@Nullable String xsd) {
			this.defaultNamespaceUrl = url;
			if (xsd != null) {
				xsds.put(url, xsd);
			}
			return this;
		}

		public CreateXMLBuilder withXmlNameSpace(@Nonnull String prefix,
				@Nonnull String url, @Nullable String xsd) {
			this.namespaces.put(prefix, url);

			if (xsd != null) {
				xsds.put(url, xsd);
			}

			return this;
		}

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

	public CreateFile(File file) {
		super();
		this.file = file;
		logger.info("Create {}", file.getAbsolutePath());
	}

	public CreateFile withInitialContents(String contents) {
		this.initialContents = contents;
		return this;
	}

	@Override
	public ActionResult perform() {
		if (file.exists()) {
			return ActionResult.ok();
		} else {
			try {
				if (!file.getParentFile().exists()) {
					if (!file.getParentFile().mkdirs()) {
						return ActionResult.error(
								"Parent directory %s does not exist and could not create",
								file.getParentFile().getAbsolutePath());
					}
				}

				if (file.createNewFile()) {
					// Check

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

	public static CreateFile emptyJavaClassFile(File file, String packageName,
			String className) {

		final StringBuilder initial = new StringBuilder();
		initial.append("package ").append(packageName).append(";\n\n");
		initial.append("public class ").append(className).append(" {\n\n");
		initial.append("}\n");

		return new CreateFile(file).withInitialContents(initial.toString());
	}

	public static CreateFile emptyNonPublicJavaClassFile(File file,
			String packageName, String className) {

		final StringBuilder initial = new StringBuilder();
		initial.append("package ").append(packageName).append(";\n\n");
		initial.append("class ").append(className).append(" {\n\n");
		initial.append("}\n");

		return new CreateFile(file).withInitialContents(initial.toString());
	}

	public static CreateFile emptyJavaInterfaceFile(File file,
			String packageName, String className) {

		final StringBuilder initial = new StringBuilder();
		initial.append("package ").append(packageName).append(";\n\n");
		initial.append("public interface ").append(className).append(" {\n\n");
		initial.append("}\n");

		return new CreateFile(file).withInitialContents(initial.toString());
	}

	public static CreateXMLBuilder emptyXmlFile(@Nonnull File file) {
		return new CreateXMLBuilder(file);
	}

	public static CreateFile emptyHtmlFile(File file,
			IHTMLFileOption... options) {
		return emptyHtmlFile(file, null, options);
	}

	public static CreateFile emptyHtmlFile(File file, Map<String, String> xmlns,
			IHTMLFileOption... options) {

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
		Arrays.stream(options).forEach(o -> o.applyTo(initial));
		initial.append("</html>");
		return new CreateFile(file).withInitialContents(initial.toString());
	}

	@FunctionalInterface
	public static interface IHTMLFileOption {
		void applyTo(StringBuilder target);
	}

	public enum DefaultHTML implements IHTMLFileOption {
		HEAD {
			@Override
			public void applyTo(StringBuilder target) {
				target.append("\t<head>\n");
				target.append("\t</head>\n");
			}
		},
		BODY {
			@Override
			public void applyTo(StringBuilder target) {
				target.append("\t<body>\n");
				target.append("\t</body>\n");
			}
		};
	}

}
