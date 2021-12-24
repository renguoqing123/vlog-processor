package com.log.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import com.sun.tools.javac.processing.JavacFiler;
import com.sun.tools.javac.code.*;

@SuppressWarnings({ "unused" })
@SupportedAnnotationTypes("com.log.processor.LogClass")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class LogProcessor extends AbstractProcessor{

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		System.out.println("---------------LogProcessor---init-----------------");
		super.init(processingEnv);
	}
	
	@Override
	public Set<String> getSupportedAnnotationTypes() {
		System.out.println("---------------LogProcessor---getSupportedAnnotationTypes--------------");
		// TODO Auto-generated method stub
//		Set<String> set = Collections.emptySet();
//		set.add("com.log.processor.LogClass");
		return super.getSupportedAnnotationTypes();
	}
	
	@SuppressWarnings({"resource" })
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		System.out.println("---------------LogProcessor---process--------------");
		Messager messager = processingEnv.getMessager();
		for(TypeElement typeElement : annotations) {
//			System.out.println(typeElement.getKind());
//			Set<String> s = getSupportedAnnotationTypes();
//			String clazz = s.stream().findFirst().get();
			for(Element e : env.getElementsAnnotatedWith(typeElement)) {
				//获取注解
                messager.printMessage(Diagnostic.Kind.WARNING, "Printing:" + e.toString());
                messager.printMessage(Diagnostic.Kind.WARNING, "Printing:" + e.getSimpleName());
                messager.printMessage(Diagnostic.Kind.WARNING, "Printing:" + e.getEnclosingElement().toString());
                
                String clazz = e.toString();
                Name clazzName = e.getSimpleName();
                String claName = clazzName.toString();
//				LogClass logBack = e.getAnnotation(LogClass.class);
//				Class<?> clazz = logBack.getClass();
				
//				Class clazz = logBack.className();
//				AnnotationMirror annotationMirror = getAnnotationMirror(e,clazz);
				
				try {
					
					String fullClassName = e.toString();
//					Symbol.ClassSymbol s = (Symbol.ClassSymbol)e;
//					JavaFileObject javafile = s.sourcefile;
	                fullClassName = fullClassName.replace('.', '\\');
//					String local = javafile.getName().toString();
					//
	                String local = System.getProperty("user.dir");
	                System.out.println(local);
	                File file = new File(local +"\\"+ fullClassName + ".class");
	                BufferedReader reader = new BufferedReader(new FileReader(file));
	                String line;
                    //
                    StringBuilder originCode = new StringBuilder();
                    boolean up = false;
                    boolean first = false;
                    while ((line = reader.readLine()) != null) {
                    	if(line.contains("{") && !up && !first) {
                    		originCode.append(line).append("\n");
//                    		System.out.println(originCode);
                    		up = true;
                    		first = true;
                    		continue;
                    	}
                    	if(up) {
                    		originCode.append("	private static final com.uhome.log.Log log = new com.uhome.log.LogAdapter();").append("\n");
                    		originCode.append(line).append("\n");
//                    		System.out.println(originCode);
                    		up = false;
                    		continue;
                    	}
                        originCode.append(line).append("\n");
//                        System.out.println(originCode);
                    }
                    System.out.println(originCode);
                    String newCode = originCode.toString();
					

	                String classPath = System.getProperty("java.class.path");
	                System.out.println(classPath);
	                String[] cStr = classPath.split(";");
	                if(cStr.length!=-1) {
	                	classPath = cStr[cStr.length-1];
	                	classPath = classPath.substring(local.length()+1, classPath.length());
	                	classPath = classPath+".";
	                }
	                System.out.println(classPath);
	                
                    JavacFiler javacFiler = (JavacFiler)processingEnv.getFiler();
                    
					JavaFileObject f = javacFiler.createClassFile(classPath + clazz, e);
					
					Writer writer = f.openWriter();
					writer.write(newCode);
					writer.flush();
					writer.close();
					javacFiler.close();
					return true;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		return false;
	}

	private AnnotationMirror getAnnotationMirror(Element e, Class<?> clazz) {
		String clazzName = clazz.getName();
		for(AnnotationMirror m : e.getAnnotationMirrors()) {
		    if(m.getAnnotationType().toString().equals(clazzName)) {
		        return m;
		    }
		}
		return null;
	}
	
	@Override
	public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation,
			ExecutableElement member, String userText) {
		System.out.println("---------------LogProcessor---getCompletions--------------");
		return super.getCompletions(element, annotation, member, userText);
	}
	
	@Override
	public SourceVersion getSupportedSourceVersion() {
		System.out.println("---------------LogProcessor---getSupportedSourceVersion--------------");
		return SourceVersion.latestSupported();
	}

	@Override
	public Set<String> getSupportedOptions() {
		System.out.println("---------------LogProcessor---getSupportedOptions--------------");
		return super.getSupportedOptions();
	}
	
	
}
