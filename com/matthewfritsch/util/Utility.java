package com.matthewfritsch.util;

import java.io.PrintWriter;

public class Utility {
    public static void writeln(PrintWriter writer, String message, int tabQty) {
        for (int x = 0; x < tabQty; ++x) {
            message = "    " + message;
        }
        writer.println(message);
    }

    public static void writeln(PrintWriter writer, String message) {
        writeln(writer, message, 0);
    }

    public static void writeln(PrintWriter writer) {
        writeln(writer, "");
    }
}
