package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;

import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.CSSProperty;
import cz.vutbr.web.css.NodeData;
import cz.vutbr.web.css.TermColor;
import cz.vutbr.web.css.TermFactory;

public class UAConformancy {
	private static Logger log = Logger.getLogger(UAConformancy.class);

	private static TermFactory tf = CSSFactory.getTermFactory();
	private static Map<Element, NodeData> decl;
	
	@BeforeClass
	public static void init() throws FileNotFoundException {

		log.info("\n\n\n == UAConformancy test == " + new Date() + " == \n\n\n");
		
		try {
		decl = CSSFactory.parse(new FileReader("data/invalid/style.css"), 
				new FileInputStream("data/invalid/style.html"), "screen", true);
		}
		catch(FileNotFoundException e) {
			log.error(e);
			throw e;
		}
	}
	
	@Test
	public void testNotEmptyness() {
		
		Assert.assertNotNull("Declarations parsed", decl);
		Assert.assertNotSame("There are some declarations", Collections.emptyMap(), decl);
		
		for(Element e: decl.keySet())
			log.debug(e.getNodeName() +  ": " + decl.get(e));
	}
	
	@Test 
	public void testH1() {

		NodeData nd = retrieve("h1");
		
		Assert.assertEquals("Color is red", tf.createColor("#ff0000"), 
				nd.getValue(TermColor.class, "color"));
		
		Assert.assertNull("There is no rotatation", 
				nd.getProperty(CSSProperty.class, "rotation"));
	}
	
	@Test 
	public void testIMG() {
		NodeData nd = retrieve("img");
		
		Assert.assertEquals("Float is left", CSSProperty.Float.LEFT, 
				nd.getProperty(CSSProperty.Float.class, "float"));
	}
	
	
	
	private NodeData retrieve(String elementName) {
		for(Element e: decl.keySet()) {
			if(elementName.equalsIgnoreCase(e.getNodeName()))
				return decl.get(e);
		}
		return null;
	}
	
}
