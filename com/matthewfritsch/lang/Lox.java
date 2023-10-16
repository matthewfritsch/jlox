package com.matthewfritsch.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;
    public static void main(String[] args) throws IOException {
        if(args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64); //see sysexits.h
        }
        else if (args.length == 1) {
            runFile(args[0]);
        }
        else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path)); //get all bytes from the file specified at `path`
        run(new String(bytes, Charset.defaultCharset()));   //run() those bytes as a string
        if (hadError) System.exit(65);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in); //read stdin
        BufferedReader reader = new BufferedReader(input); //read input as str

        System.out.println("jlox interpreter on linux");

        while(true){
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    /*
    NOTE: `var language = "lox";` is a valid line of Lox. The
        lexemes in this line are ["var", "language", "=", "\"lox\""]
        where a lexeme is the raw substring of code. A "token" is
        more than just the raw substring, it includes the lexeme and
        other stuff.
    */
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for(Token token : tokens) {
            System.out.println(token);
        }
    }

    /*
    TODO: It's good to have error reporting. This is not great.
        Should probably implement something a bit more descriptive,
        as well as a stack to work backwards through. This is the
        right place for error *reporting*, but not the right place
        for error *generation*. TBD.
    */
    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println(
            "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

}
