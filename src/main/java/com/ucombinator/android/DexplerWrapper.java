package com.ucombinator.android;

import java.util.Collections;
import java.util.Iterator;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

import soot.Main;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Sources;
import soot.options.Options;
import soot.util.Chain;

public class DexplerWrapper {
	
	public static Chain<SootClass> getAst() {
		return Scene.v().getApplicationClasses();
	}
	
	public static CallGraph getCallGraph() {
		return Scene.v().getCallGraph();	
	}
	
	public static Iterator<SootMethod> getInternalCallers() {
		CallGraph cg = getCallGraph();
		Iterator<Edge> sources = cg.listener();
		Iterator<Edge> appMethods = Iterators.filter(sources, new Predicate<Edge>() {
			public boolean apply(Edge e) {
				System.out.println(e.src());
				return !e.src().isJavaLibraryMethod() && !e.src().method().getDeclaringClass().isLibraryClass();
			}
		});
		
		Iterator<SootMethod> callers = new Sources(appMethods);
		return Sets.newHashSet(callers).iterator();
	}
	
	public static void parseApk(String androidJars, String apk) {
		Options.v().set_verbose(true);
		/* Soot options */
		Options.v().set_no_bodies_for_excluded(true);
		/* Set the apk to process */
		Options.v().set_process_dir(Collections.singletonList(apk));
		/* The source bytecode is an android apk */
		Options.v().set_src_prec(Options.src_prec_apk);
		/* we need to link instructions to source line for display */
		Options.v().set_keep_line_number(true);
		/* The android libs to compile against (it will choose the proper one from the AndroidManifest.xml */
		Options.v().set_android_jars(androidJars);
		/* We are not outputting a code transformation */
		Options.v().set_output_format(Options.output_format_none);
		/* For callgraph, we need whole program */
		Options.v().set_whole_program(true);
		/* Called methods without jar files or source are considered phantom */
		Options.v().set_allow_phantom_refs(true);
		
		Options.v().set_dump_cfg(Collections.singletonList("cg"));
		String classPath = Scene.v().getAndroidJarPath(androidJars, apk);
		Options.v().set_soot_classpath(classPath);
		/* Apply these options */
		Main.v().autoSetOptions();
        	/* This will parse the apk file into soot classes */
		Scene.v().loadNecessaryClasses();	
		
		/* The callgraph phase options */
		/* assume all methods are reachable to avoid setting explicit entry points */
		Options.v().setPhaseOption("cg", "all-reachable:true");
		//Options.v().setPhaseOption("cg.spark", "enabled:true");
		/* run the callgraph builder */
		PackManager.v().getPack("cg").apply();
	}
	
	
}
