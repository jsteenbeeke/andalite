package com.jeroensteenbeeke.andalite.transformation;

import java.io.File;

import javax.annotation.Nonnull;

import com.jeroensteenbeeke.andalite.ActionResult;
import com.jeroensteenbeeke.andalite.CodePoint;
import com.jeroensteenbeeke.andalite.FileRewriter;
import com.jeroensteenbeeke.andalite.Location;
import com.jeroensteenbeeke.andalite.analyzer.Locatable;

public class Transformation {
	private final CodePoint point;

	private final String code;

	private Transformation(CodePoint point, String code) {
		super();
		this.point = point;
		this.code = code;
	}

	public ActionResult applyTo(@Nonnull File targetFile) {
		FileRewriter rewriter = new FileRewriter(targetFile);
		rewriter.insert(point, code);
		return rewriter.rewrite();
	}

	public static Transformation insertBefore(@Nonnull Locatable locatable,
			String code) {
		return insertBefore(locatable.getLocation(), code);
	}

	public static Transformation insertBefore(@Nonnull Location location,
			String code) {
		return new Transformation(location.getStart(), code);
	}

	public static Transformation insertAfter(@Nonnull Locatable locatable,
			String code) {
		return insertAfter(locatable.getLocation(), code);
	}

	public static Transformation insertAfter(Location location, String code) {
		return new Transformation(location.getEnd(), code);
	}

}