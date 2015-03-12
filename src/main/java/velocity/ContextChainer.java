package velocity;

import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

public class ContextChainer {
		private VelocityContext context = new VelocityContext();
		
		public Context getContext() {
			return context;
		}

		public ContextChainer chain(Map<String, Object> map) {
			context = new VelocityContext(map, context); 
			return this;
		}
	}
