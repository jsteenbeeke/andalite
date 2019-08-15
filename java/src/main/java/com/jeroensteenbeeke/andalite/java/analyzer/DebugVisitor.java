package com.jeroensteenbeeke.andalite.java.analyzer;

import com.github.javaparser.Position;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.modules.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.slf4j.Logger;


public class DebugVisitor extends VoidVisitorAdapter<Logger> {
	private int indent = 0;

	private void visitNode(Node n, Logger arg) {
		String prefix = "";
		for (int i = 0; i < indent; i++) {
			prefix = "\t".concat(prefix);
		}


		arg.debug(String.format("%s%s [%s - %s]", prefix, n.toString(),
								n.getBegin().map(Position::toString).orElse("unknown start"),
								n.getEnd().map(Position::toString).orElse("unknown end")
		));
	}

	@Override
	public void visit(CompilationUnit n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(PackageDeclaration n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ImportDeclaration n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(TypeParameter n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, Logger arg) {
		indent++;
		visitNode(n, arg);
		indent--;
	}

	@Override
	public void visit(EnumDeclaration n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(EnumConstantDeclaration n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(AnnotationDeclaration n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(AnnotationMemberDeclaration n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(FieldDeclaration n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(VariableDeclarator n, Logger arg) {
		visitNode(n, arg);

	}


	@Override
	public void visit(ConstructorDeclaration n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(MethodDeclaration n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(Parameter n, Logger arg) {
		visitNode(n, arg);

	}


	public void visit(InitializerDeclaration n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ClassOrInterfaceType n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(PrimitiveType n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(VoidType n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(WildcardType n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ArrayAccessExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ArrayCreationExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ArrayInitializerExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(AssignExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(BinaryExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(CastExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ClassExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ConditionalExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(EnclosedExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(FieldAccessExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(InstanceOfExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(StringLiteralExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(IntegerLiteralExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(LongLiteralExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(CharLiteralExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(DoubleLiteralExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(BooleanLiteralExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(NullLiteralExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(LambdaExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(MethodCallExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(NameExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ObjectCreationExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ThisExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(SuperExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(UnaryExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(VariableDeclarationExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(MarkerAnnotationExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(SingleMemberAnnotationExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(NormalAnnotationExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(MemberValuePair n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(MethodReferenceExpr n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(AssertStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(BlockStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(LabeledStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(EmptyStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ExpressionStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(SwitchStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(BreakStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ReturnStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(IfStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(WhileStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ContinueStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(DoStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ForStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(ThrowStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(SynchronizedStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(TryStmt n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(CatchClause n, Logger arg) {
		visitNode(n, arg);

	}

	@Override
	public void visit(LineComment n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(BlockComment n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(JavadocComment n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(ForEachStmt n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(Name n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(ArrayType n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(ArrayCreationLevel n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(IntersectionType n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(LocalClassDeclarationStmt n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(ModuleDeclaration n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(ModuleRequiresDirective n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(ModuleExportsDirective n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(ModuleProvidesDirective n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(ModuleUsesDirective n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(ModuleOpensDirective n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(Modifier n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(SimpleName n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(UnionType n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(SwitchEntry n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(UnknownType n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(TypeExpr n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(UnparsableStmt n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(ReceiverParameter n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(VarType n, Logger arg) {
		visitNode(n, arg);
	}

	@Override
	public void visit(SwitchExpr n, Logger arg) {
		visitNode(n, arg);
	}
}
