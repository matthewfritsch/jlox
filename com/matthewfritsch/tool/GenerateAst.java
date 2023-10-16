package com.matthewfritsch.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import com.matthewfritsch.util.Utility;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output_directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
            "Binary   : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal  : Object value",
            "Unary    : Token operator, Expr right"
        ));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        Utility.writeln(writer, "package com.matthewfritsch.lang;");
        Utility.writeln(writer);
        Utility.writeln(writer, "import java.util.List;");
        Utility.writeln(writer);
        Utility.writeln(writer, "abstract class " + baseName + "{");

        defineVisitor(writer, baseName, types);

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        Utility.writeln(writer);
        Utility.writeln(writer, "abstract <R> R accept(Visitor<R> visitor);", 1);

        Utility.writeln(writer, "}");
        writer.close();
    }

    // visitor pattern
    // We're defining he visitor interface here so each Expr can have an "accept" fn.
    // The accept fn will work differently for each Expr type, but they all need to be
    // able to do this.
    //
    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        Utility.writeln(writer, "interface Visitor<R> {", 1);

        for (String type: types) {
            String typeName = type.split(":")[0].trim();
            Utility.writeln(writer, 
                "R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");",
                2);
        }

        Utility.writeln(writer, "}", 1);
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        Utility.writeln(writer, "static class " + className + " extends " + baseName + " {", 1);

        //ctr
        Utility.writeln(writer, className + "(" + fieldList + ") {", 2);

        //Store params in fields
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            Utility.writeln(writer, "this." + name + " = " + name + ";", 3);
        }

        Utility.writeln(writer, "}", 2);

        //visitor pattern
        Utility.writeln(writer);
        Utility.writeln(writer, "@Override", 2);
        Utility.writeln(writer, "<R> R accept(Visitor<R> visitor) {", 2);
        Utility.writeln(writer, "return visitor.visit" + className + baseName + "(this);" , 3);
        Utility.writeln(writer, "}", 2);

        //Fields
        Utility.writeln(writer);
        for (String field : fields) {
            Utility.writeln(writer, "final " + field + ";", 2);
        }

        Utility.writeln(writer, "}", 1);
    }
    
}
