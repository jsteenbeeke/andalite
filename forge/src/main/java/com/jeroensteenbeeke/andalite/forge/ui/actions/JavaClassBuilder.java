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

import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

public class JavaClassBuilder {
	public static class TypeDesc {
		private final String packageName;

		private final String name;

		private TypeDesc(String packageName, String name) {
			super();
			this.packageName = packageName;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public String getPackageName() {
			return packageName;
		}
	}

	private final String type;

	private final String packageName;

	private final String typeName;

	private final Set<String> superClasses = Sets.newTreeSet();

	private final Set<String> implementedInterfaces = Sets.newTreeSet();

	private final Set<String> imported = Sets.newTreeSet();

	private final Set<String> initialAnnotations = Sets.newTreeSet();

	private JavaClassBuilder(String type, String packageName, String typeName) {
		this.type = type;
		this.packageName = packageName;
		this.typeName = typeName;
	}

	public JavaClassBuilder importing(String fqdn) {
		this.imported.add(fqdn);

		return this;
	}

	public JavaClassBuilder withSuperclass(String fqdn) {
		handleType(fqdn, this.superClasses);

		return this;
	}

	public JavaClassBuilder withImplementedInterface(String fqdn) {
		handleType(fqdn, this.implementedInterfaces);

		return this;
	}

	public JavaClassBuilder withAnnotation(String format, Object... params) {
		String annotation = format(format, params);
		int lparen = annotation.indexOf('(');
		String fqdn = lparen != -1 ? annotation.substring(0, lparen)
				: annotation;
		String extra = lparen != -1 ? annotation.substring(lparen) : "";

		handleType(fqdn, this.initialAnnotations, extra);

		return this;
	}

	private static String format(String format, Object[] params) {
		if (params.length == 0) {
			return format;
		}

		return String.format(format, params);
	}

	private void handleType(String fqdn, Set<String> target) {
		handleType(fqdn, target, "");
	}

	private void handleType(String fqdn, Set<String> target, String extra) {
		TypeDesc desc = parse(fqdn);

		boolean useFullFQDN = false;

		if (typeName.equals(desc.getName())) {
			// Use full FQDN in reference
			useFullFQDN = true;
		} else {
			// Check imports to see if name is already taken
			for (String s : this.imported) {
				TypeDesc d = parse(s);
				if (d.getName().equals(desc.getName())
						&& !d.getPackageName().equals(desc.getPackageName())) {
					useFullFQDN = true;
					break;
				}
			}
		}

		if (useFullFQDN) {
			target.add(fqdn.concat(extra));
		} else {
			target.add(desc.name.concat(extra));

			if (!"java.lang".equals(desc.getPackageName())) {
				this.imported.add(fqdn);
			}

		}

	}

	public String toJava() {
		StringBuilder java = new StringBuilder();

		java.append("package ").append(packageName).append(";\n\n");

		for (String imp : imported) {
			java.append("import ").append(imp).append(";\n");
		}

		java.append("\n");

		for (String annotation : initialAnnotations) {
			java.append("@").append(annotation).append("\n");
		}
		java.append("public ").append(type).append(" ").append(typeName);

		if (!superClasses.isEmpty()) {
			java.append(" extends ");
			Joiner.on(", ").appendTo(java, superClasses);
		}

		if (!implementedInterfaces.isEmpty()) {
			java.append(" implements ");
			Joiner.on(", ").appendTo(java, implementedInterfaces);
		}

		java.append(" {\n\n");
		java.append("}\n");

		return java.toString();
	}

	private TypeDesc parse(String fqdn) {
		int idx = fqdn.lastIndexOf('.');

		if (idx == -1) {
			return new TypeDesc("java.lang", fqdn);
		}

		return new TypeDesc(fqdn.substring(0, idx), fqdn.substring(idx + 1));
	}

	public static Stage1 newInterface() {
		return new Stage1("interface");
	}

	public static Stage1 newClass() {
		return new Stage1("class");
	}

	public static class Stage1 {
		private final String type;

		private Stage1(String type) {
			super();
			this.type = type;
		}

		public Stage2 inPackage(String packageName, Object... params) {
			return new Stage2(this, format(packageName, params));
		}
	}

	public static class Stage2 {
		private final Stage1 stage1;

		private final String packageName;

		private Stage2(Stage1 stage1, String packageName) {
			super();
			this.stage1 = stage1;
			this.packageName = packageName;
		}

		public JavaClassBuilder named(String typeName, Object... params) {
			return new JavaClassBuilder(stage1.type, packageName, format(
					typeName, params));
		}

	}
}
