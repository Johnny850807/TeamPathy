package com.ood.clean.waterball.teampathy.Domain.Model.WBS;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static com.ood.clean.waterball.teampathy.MyUtils.EnglishAbbrDateConvert.timeToDate;


public class TaskXmlTranslatorImp implements TaskXmlTranslator {
    public static final String TASK_GROUP_NAME = "TaskGroup";
    public static final String TASK_NAME = "Task";

    public static final String NAME_ATT = "Name";
    public static final String STARTDATE_ATT = "StartDate";
    public static final String ENDDATE_ATT = "EndDate";
    public static final String DESCRIPTION_ATT = "Description";
    public static final String CONTRIBUTION_ATT = "Contribution";
    public static final String DEPENDENCY_ATT = "Dependency";
    public static final String STATUS_ATT = "Status";
    public static final String ASSIGNED_ID_ATT = "AssignedId";

	public String taskToXml(TaskItem taskRoot) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.newDocument();
        document.appendChild(taskRoot.toXmlNode(document));

        StringWriter stringWriter = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
		return stringWriter.toString();
	}

	public TaskItem xmlToTasks(String xml) throws ParserConfigurationException, IOException, SAXException, ParseException {
        Document document = parseDocument(xml);
        Element rootElement = document.getDocumentElement();

        String name = rootElement.getAttribute(NAME_ATT);
        TaskItem taskRoot = new TaskRoot(name);

		addAllTaskChild(taskRoot,rootElement);
        return taskRoot;
	}

	private void addAllTaskChild(TaskItem taskGroup, Element element) throws ParseException {
        NodeList nodeList = element.getChildNodes();
        for ( int i = 0 ; i < nodeList.getLength() ; i ++ )
        {
            Node node = nodeList.item(i);
            if ( node.getNodeType() == Node.ELEMENT_NODE )
                taskGroup.addTaskChild(createTaskItem((Element) node));
        }
    }

	private TaskItem createTaskItem(Element element) throws ParseException {
        TaskItem taskItem;
        String name = element.getAttribute(NAME_ATT);
        String ofGroup = ((Element)element.getParentNode()).getAttribute(NAME_ATT);

        if (element.getTagName().equals(TASK_GROUP_NAME))
        {
            taskItem = new TaskGroup(name,ofGroup);
            addAllTaskChild(taskItem,element);
        }
        else
            taskItem = parseElementToTodoTask(name,ofGroup,element);

        return taskItem;
    }

    private TodoTask parseElementToTodoTask(String name, String ofGroup, Element element) throws ParseException {
        String description = element.getAttribute(DESCRIPTION_ATT);
        TodoTask.Status status = TodoTask.Status.valueOf(element.getAttribute(STATUS_ATT).toUpperCase());
        int contribution = Integer.parseInt(element.getAttribute(CONTRIBUTION_ATT));
        String dependency = element.getAttribute(DEPENDENCY_ATT);
        int assignedId = Integer.parseInt(element.getAttribute(ASSIGNED_ID_ATT));
        Date startDate = timeToDate(element.getAttribute(STARTDATE_ATT));
        Date endDate = timeToDate(element.getAttribute(ENDDATE_ATT));

        return new TodoTask(name, ofGroup, description, contribution, startDate, endDate, dependency, status, assignedId);
    }

	private Document parseDocument(String xml) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        is.setEncoding("UTF-8");
        return builder.parse(is);
    }
}
