package velocity;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.junit.Test;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class BasicUsage {
	private Template createTemplate(String path) {
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
		ve.setProperty("class.resource.loader.class",
		               "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		ve.init();

		return ve.getTemplate(path);
	}
	
	@Test
	public void helloWorld() {
		Template template = createTemplate("velocity/HelloWorld.vm");

		VelocityContext context = new VelocityContext();
        context.put("name", "World");
        StringWriter writer = new StringWriter();

        template.merge(context, writer);
        assertThat(writer.toString(), is("Hello World! Welcome to Velocity!"));
	}
	
	@Test
	public void testContextPriority() {
		Template template = createTemplate("velocity/ContextPriority.vm");
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> map3 = new HashMap<String, Object>();
		
		map1.put("name", "Alice");
		map2.put("name", "Bob");
		map3.put("name", "John");
		
		ContextChainer chainer = new ContextChainer();
		chainer.chain(map1).chain(map2).chain(map3);
		StringWriter writer = new StringWriter();

		template.merge(chainer.getContext(), writer);
		assertThat(writer.toString(), is("John"));
		
		StringWriter writer2 = new StringWriter();
		ContextChainer chainer2 = new ContextChainer();
		chainer2.chain(map3).chain(map2).chain(map1);
		
		template.merge(chainer2.getContext(), writer2);
		assertThat(writer2.toString(), is("Alice"));
	}
	
	@Test
	public void testContextPriority_NullIsSkiped() {
		Template template = createTemplate("velocity/ContextPriority.vm");
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> map3 = new HashMap<String, Object>();
		
		map1.put("name", "Bob");
		map2.put("name", "Alice");
		map3.put("name", null);
		
		ContextChainer chainer = new ContextChainer();
		chainer.chain(map1).chain(map2).chain(map3);
		StringWriter writer = new StringWriter();

		template.merge(chainer.getContext(), writer);
		assertThat(writer.toString(), is("Alice"));
		
		map1.put("name", null);
		map2.put("name", "Alice");
		map3.put("name", "");
		
		ContextChainer chainer2 = new ContextChainer();
		chainer.chain(map1).chain(map2).chain(map3);
		StringWriter writer2 = new StringWriter();

		template.merge(chainer2.getContext(), writer);
		assertThat(writer2.toString(), is(""));
	}
}
