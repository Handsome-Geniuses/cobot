package com.handsome.nosnhoj.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.URCapInfo;
import com.ur.urcap.api.domain.function.Function;
import com.ur.urcap.api.domain.function.FunctionException;
import com.ur.urcap.api.domain.function.FunctionModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.variable.GlobalVariable;

public class util {
	//concatenates any types together.
	public static String str(Object... objs) {
		String out = "";
		try {
			for (int i = 0; i < objs.length; i++) {
//				out=out+String.valueOf(objs[i]);
				out=out+objs[i];
			}
			return out;
		} 
		catch (Exception e) {
			System.out.println("ERROR: nope");
			return "nope";
		}
	}
	//surrounds argument with quotes
	public static <T> String quote(T t) {
		return "\""+String.valueOf(t)+"\"";
	}
	//surrounds argument with parenthesis
	public static <T> String arg(T t) {
		return "("+String.valueOf(t)+")";
	}
	//surrounds argument with quotes then parenthesis
	public static <T> String argquote(T t) {
		return arg(quote(t));
	}
	public static <T> String eq(String s, T exp) {
		return s+"="+String.valueOf(exp);
	}
	public static <T> String fn(String fn_name, T exp) {
		return fn_name+arg(exp);
	}	
	//reads file line by line and stores in string array
	public static String[] RawRead(String file) throws IOException {
		List<String> list = new ArrayList<String>();
		BufferedReader read = new BufferedReader(new FileReader(file));
		String line = read.readLine();
		while(line!=null) {
			list.add(line);
			line = read.readLine();
		}
		read.close();
		String out[] = new String[list.size()];
		list.toArray(out);
		return out;
	}
	//reads raw file and appends to writer
	public static void RawWriter(ScriptWriter writer, String file) throws IOException {
		String r[] = RawRead(file);
		for (int i = 0; i < r.length; i++) {
			writer.appendLine(r[i]);
		}
	}
	//appends given string array to writer
	public static void RawWriter(ScriptWriter writer, String s[]) {
		for (int i = 0; i < s.length; i++) {
			writer.appendLine(s[i]);
		}
	}
	
	public static void AddScriptFunction(InstallationAPIProvider apiProvider, String name, String... args) {
		//grab list of functions
		FunctionModel model = apiProvider.getInstallationAPI().getFunctionModel();
		//does a function with that name already exist?
		if(model.getFunction(name)==null) {
			//add function
			try {
				model.addFunction(name, args);
			} catch (FunctionException e) {
				e.printStackTrace();
			}
		}	
	}
	
	//The same as GlobalVariable but can now create with one line.
	public static class GV implements GlobalVariable{
		private final String name;
		public GV(String name) {
			this.name = name;
		}
		@Override
		public Type getType() {
			return Type.GLOBAL;
		}

		@Override
		public String getDisplayName() {	
			return name;
		}
	}
	
	
	
}
