

import org.apache.commons.collections15.Factory;

public class GraphElements {
    
    /** Creates a new instance of GraphElements */
    public GraphElements() {
    }
    
    public static class MyVertex {
        private String name;
        
        public MyVertex(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        public String toString() {
            return name;
        }
    }
    
    public static class MyEdge {

    	private double weight;
        private String name;

        public MyEdge(String name) {
            this.name = name;
        }
        

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }             
        
        public String toString() {
            return weight+"";
        }
    }
    
    // Single factory for creating Vertices...
    public static class MyVertexFactory implements Factory<MyVertex> {
        private static int nodeCount = 0;

        private static MyVertexFactory instance = new MyVertexFactory();
        
        private MyVertexFactory() {            
        }
        
        public static MyVertexFactory getInstance() {
            return instance;
        }
        
        public GraphElements.MyVertex create() {
//            String name = "Node" + nodeCount++;
            String name = ""+ nodeCount++;
            MyVertex v = new MyVertex(name);
            return v;
        }        
    }
    
    // Singleton factory for creating Edges...
    public static class MyEdgeFactory implements Factory<MyEdge> {
        private static int linkCount = 0;
        private static double defaultWeight;

        private static MyEdgeFactory instance = new MyEdgeFactory();
        
        private MyEdgeFactory() {            
        }
        
        public static MyEdgeFactory getInstance() {
            return instance;
        }
        
        public GraphElements.MyEdge create() {
        	
            String name = "Edge " + linkCount++;
            MyEdge link = new MyEdge(name);
            link.setWeight(defaultWeight);
            return link;
        }    

        public static double getDefaultWeight() {
            return defaultWeight;
        }

        public static void setDefaultWeight(double aDefaultWeight) {
            defaultWeight = aDefaultWeight;
        }

        
    }

}
