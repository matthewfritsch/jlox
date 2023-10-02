package com.matthewfritsch.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Lox {
    public static void main(String[] args) {
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
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for(Token token : tokens) {
            System.out.println(token);
        }
    }

}
