package com.jeroensteenbeeke.andalite.java.analyzer;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;

import com.github.antlrjavaparser.api.BlockComment;
import com.github.antlrjavaparser.api.Comment;
import com.github.antlrjavaparser.api.CompilationUnit;
import com.github.antlrjavaparser.api.ImportDeclaration;
import com.github.antlrjavaparser.api.LineComment;
import com.github.antlrjavaparser.api.Node;
import com.github.antlrjavaparser.api.PackageDeclaration;
import com.github.antlrjavaparser.api.TypeParameter;
import com.github.antlrjavaparser.api.body.AnnotationDeclaration;
import com.github.antlrjavaparser.api.body.AnnotationMemberDeclaration;
import com.github.antlrjavaparser.api.body.CatchParameter;
import com.github.antlrjavaparser.api.body.ClassOrInterfaceDeclaration;
import com.github.antlrjavaparser.api.body.ConstructorDeclaration;
import com.github.antlrjavaparser.api.body.EmptyMemberDeclaration;
import com.github.antlrjavaparser.api.body.EmptyTypeDeclaration;
import com.github.antlrjavaparser.api.body.EnumConstantDeclaration;
import com.github.antlrjavaparser.api.body.EnumDeclaration;
import com.github.antlrjavaparser.api.body.FieldDeclaration;
import com.github.antlrjavaparser.api.body.InitializerDeclaration;
import com.github.antlrjavaparser.api.body.JavadocComment;
import com.github.antlrjavaparser.api.body.MethodDeclaration;
import com.github.antlrjavaparser.api.body.Parameter;
import com.github.antlrjavaparser.api.body.Resource;
import com.github.antlrjavaparser.api.body.VariableDeclarator;
import com.github.antlrjavaparser.api.body.VariableDeclaratorId;
import com.github.antlrjavaparser.api.expr.ArrayAccessExpr;
import com.github.antlrjavaparser.api.expr.ArrayCreationExpr;
import com.github.antlrjavaparser.api.expr.ArrayInitializerExpr;
import com.github.antlrjavaparser.api.expr.AssignExpr;
import com.github.antlrjavaparser.api.expr.BinaryExpr;
import com.github.antlrjavaparser.api.expr.BooleanLiteralExpr;
import com.github.antlrjavaparser.api.expr.CastExpr;
import com.github.antlrjavaparser.api.expr.CharLiteralExpr;
import com.github.antlrjavaparser.api.expr.ClassExpr;
import com.github.antlrjavaparser.api.expr.ConditionalExpr;
import com.github.antlrjavaparser.api.expr.DoubleLiteralExpr;
import com.github.antlrjavaparser.api.expr.EnclosedExpr;
import com.github.antlrjavaparser.api.expr.FieldAccessExpr;
import com.github.antlrjavaparser.api.expr.InstanceOfExpr;
import com.github.antlrjavaparser.api.expr.IntegerLiteralExpr;
import com.github.antlrjavaparser.api.expr.IntegerLiteralMinValueExpr;
import com.github.antlrjavaparser.api.expr.LambdaExpr;
import com.github.antlrjavaparser.api.expr.LongLiteralExpr;
import com.github.antlrjavaparser.api.expr.LongLiteralMinValueExpr;
import com.github.antlrjavaparser.api.expr.MarkerAnnotationExpr;
import com.github.antlrjavaparser.api.expr.MemberValuePair;
import com.github.antlrjavaparser.api.expr.MethodCallExpr;
import com.github.antlrjavaparser.api.expr.MethodReferenceExpr;
import com.github.antlrjavaparser.api.expr.NameExpr;
import com.github.antlrjavaparser.api.expr.NormalAnnotationExpr;
import com.github.antlrjavaparser.api.expr.NullLiteralExpr;
import com.github.antlrjavaparser.api.expr.ObjectCreationExpr;
import com.github.antlrjavaparser.api.expr.QualifiedNameExpr;
import com.github.antlrjavaparser.api.expr.SingleMemberAnnotationExpr;
import com.github.antlrjavaparser.api.expr.StringLiteralExpr;
import com.github.antlrjavaparser.api.expr.SuperExpr;
import com.github.antlrjavaparser.api.expr.ThisExpr;
import com.github.antlrjavaparser.api.expr.UnaryExpr;
import com.github.antlrjavaparser.api.expr.VariableDeclarationExpr;
import com.github.antlrjavaparser.api.stmt.AssertStmt;
import com.github.antlrjavaparser.api.stmt.BlockStmt;
import com.github.antlrjavaparser.api.stmt.BreakStmt;
import com.github.antlrjavaparser.api.stmt.CatchClause;
import com.github.antlrjavaparser.api.stmt.ContinueStmt;
import com.github.antlrjavaparser.api.stmt.DoStmt;
import com.github.antlrjavaparser.api.stmt.EmptyStmt;
import com.github.antlrjavaparser.api.stmt.ExplicitConstructorInvocationStmt;
import com.github.antlrjavaparser.api.stmt.ExpressionStmt;
import com.github.antlrjavaparser.api.stmt.ForStmt;
import com.github.antlrjavaparser.api.stmt.ForeachStmt;
import com.github.antlrjavaparser.api.stmt.IfStmt;
import com.github.antlrjavaparser.api.stmt.LabeledStmt;
import com.github.antlrjavaparser.api.stmt.ReturnStmt;
import com.github.antlrjavaparser.api.stmt.SwitchEntryStmt;
import com.github.antlrjavaparser.api.stmt.SwitchStmt;
import com.github.antlrjavaparser.api.stmt.SynchronizedStmt;
import com.github.antlrjavaparser.api.stmt.ThrowStmt;
import com.github.antlrjavaparser.api.stmt.TryStmt;
import com.github.antlrjavaparser.api.stmt.TypeDeclarationStmt;
import com.github.antlrjavaparser.api.stmt.WhileStmt;
import com.github.antlrjavaparser.api.type.ClassOrInterfaceType;
import com.github.antlrjavaparser.api.type.PrimitiveType;
import com.github.antlrjavaparser.api.type.ReferenceType;
import com.github.antlrjavaparser.api.type.VoidType;
import com.github.antlrjavaparser.api.type.WildcardType;
import com.github.antlrjavaparser.api.visitor.VoidVisitor;

public class DebugVisitor implements VoidVisitor<Logger> {
	private int indent = 0;

	private void visitNode(Node n, Logger arg) {
		String prefix = "";
		for (int i = 0; i < indent; i++) {
			prefix = "\t".concat(prefix);
		}
		
		
		arg.debug(prefix.concat(n.toString()));
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
	public void visit(EmptyTypeDeclaration n, Logger arg) {
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
	public void visit(VariableDeclaratorId n, Logger arg) {
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

	@Override
	public void visit(CatchParameter n, Logger arg) {
		visitNode(n, arg);
		
	}

	@Override
	public void visit(Resource n, Logger arg) {
		visitNode(n, arg);
		
	}

	@Override
	public void visit(EmptyMemberDeclaration n, Logger arg) {
		visitNode(n, arg);
		
	}

	@Override
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
	public void visit(ReferenceType n, Logger arg) {
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
	public void visit(IntegerLiteralMinValueExpr n, Logger arg) {
		visitNode(n, arg);
		
	}

	@Override
	public void visit(LongLiteralMinValueExpr n, Logger arg) {
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
	public void visit(QualifiedNameExpr n, Logger arg) {
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
	public void visit(TypeDeclarationStmt n, Logger arg) {
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
	public void visit(SwitchEntryStmt n, Logger arg) {
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
	public void visit(ForeachStmt n, Logger arg) {
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
	}

	@Override
	public void visit(BlockComment n, Logger arg) {
	}

	@Override
	public void visit(Comment n, Logger arg) {
		
	}

	@Override
	public void visit(JavadocComment n, Logger arg) {
		
	}

	

}
