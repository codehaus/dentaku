package org.omg.uml.diagraminterchange;

/**
 * Diagram_Interchange package interface.
 * Diagram Interchange for UML
 */
public interface DiagramInterchangePackage extends javax.jmi.reflect.RefPackage {
    /**
     * Returns DiagramElement class proxy object.
     * @return DiagramElement class proxy object.
     */
    public org.omg.uml.diagraminterchange.DiagramElementClass getDiagramElement();
    /**
     * Returns GraphElement class proxy object.
     * @return GraphElement class proxy object.
     */
    public org.omg.uml.diagraminterchange.GraphElementClass getGraphElement();
    /**
     * Returns SemanticModelBridge class proxy object.
     * @return SemanticModelBridge class proxy object.
     */
    public org.omg.uml.diagraminterchange.SemanticModelBridgeClass getSemanticModelBridge();
    /**
     * Returns GraphEdge class proxy object.
     * @return GraphEdge class proxy object.
     */
    public org.omg.uml.diagraminterchange.GraphEdgeClass getGraphEdge();
    /**
     * Returns GraphNode class proxy object.
     * @return GraphNode class proxy object.
     */
    public org.omg.uml.diagraminterchange.GraphNodeClass getGraphNode();
    /**
     * Returns GraphConnector class proxy object.
     * @return GraphConnector class proxy object.
     */
    public org.omg.uml.diagraminterchange.GraphConnectorClass getGraphConnector();
    /**
     * Returns LeafElement class proxy object.
     * @return LeafElement class proxy object.
     */
    public org.omg.uml.diagraminterchange.LeafElementClass getLeafElement();
    /**
     * Returns Reference class proxy object.
     * @return Reference class proxy object.
     */
    public org.omg.uml.diagraminterchange.ReferenceClass getReference();
    /**
     * Returns TextElement class proxy object.
     * @return TextElement class proxy object.
     */
    public org.omg.uml.diagraminterchange.TextElementClass getTextElement();
    /**
     * Returns GraphicPrimitive class proxy object.
     * @return GraphicPrimitive class proxy object.
     */
    public org.omg.uml.diagraminterchange.GraphicPrimitiveClass getGraphicPrimitive();
    /**
     * Returns Polyline class proxy object.
     * @return Polyline class proxy object.
     */
    public org.omg.uml.diagraminterchange.PolylineClass getPolyline();
    /**
     * Returns Ellipse class proxy object.
     * @return Ellipse class proxy object.
     */
    public org.omg.uml.diagraminterchange.EllipseClass getEllipse();
    /**
     * Returns Image class proxy object.
     * @return Image class proxy object.
     */
    public org.omg.uml.diagraminterchange.ImageClass getImage();
    /**
     * Returns Property class proxy object.
     * @return Property class proxy object.
     */
    public org.omg.uml.diagraminterchange.PropertyClass getProperty();
    /**
     * Returns SimpleSemanticModelElement class proxy object.
     * @return SimpleSemanticModelElement class proxy object.
     */
    public org.omg.uml.diagraminterchange.SimpleSemanticModelElementClass getSimpleSemanticModelElement();
    /**
     * Returns Uml1SemanticModelBridge class proxy object.
     * @return Uml1SemanticModelBridge class proxy object.
     */
    public org.omg.uml.diagraminterchange.Uml1SemanticModelBridgeClass getUml1SemanticModelBridge();
    /**
     * Returns CoreSemanticModelBridge class proxy object.
     * @return CoreSemanticModelBridge class proxy object.
     */
    public org.omg.uml.diagraminterchange.CoreSemanticModelBridgeClass getCoreSemanticModelBridge();
    /**
     * Returns DiagramLink class proxy object.
     * @return DiagramLink class proxy object.
     */
    public org.omg.uml.diagraminterchange.DiagramLinkClass getDiagramLink();
    /**
     * Returns Diagram class proxy object.
     * @return Diagram class proxy object.
     */
    public org.omg.uml.diagraminterchange.DiagramClass getDiagram();
    /**
     * Returns ADiagramElementProperty association proxy object.
     * @return ADiagramElementProperty association proxy object.
     */
    public org.omg.uml.diagraminterchange.ADiagramElementProperty getADiagramElementProperty();
    /**
     * Returns AGraphElementSemanticModel association proxy object.
     * @return AGraphElementSemanticModel association proxy object.
     */
    public org.omg.uml.diagraminterchange.AGraphElementSemanticModel getAGraphElementSemanticModel();
    /**
     * Returns AReferenceReferenced association proxy object.
     * @return AReferenceReferenced association proxy object.
     */
    public org.omg.uml.diagraminterchange.AReferenceReferenced getAReferenceReferenced();
    /**
     * Returns AGraphElementLink association proxy object.
     * @return AGraphElementLink association proxy object.
     */
    public org.omg.uml.diagraminterchange.AGraphElementLink getAGraphElementLink();
    /**
     * Returns AContainerContained association proxy object.
     * @return AContainerContained association proxy object.
     */
    public org.omg.uml.diagraminterchange.AContainerContained getAContainerContained();
    /**
     * Returns AGraphElementAnchorage association proxy object.
     * @return AGraphElementAnchorage association proxy object.
     */
    public org.omg.uml.diagraminterchange.AGraphElementAnchorage getAGraphElementAnchorage();
    /**
     * Returns AGraphEdgeAnchor association proxy object.
     * @return AGraphEdgeAnchor association proxy object.
     */
    public org.omg.uml.diagraminterchange.AGraphEdgeAnchor getAGraphEdgeAnchor();
    /**
     * Returns ADiagramLinkDiagram association proxy object.
     * @return ADiagramLinkDiagram association proxy object.
     */
    public org.omg.uml.diagraminterchange.ADiagramLinkDiagram getADiagramLinkDiagram();
    /**
     * Returns AUml1SemanticModelBridgeElement association proxy object.
     * @return AUml1SemanticModelBridgeElement association proxy object.
     */
    public org.omg.uml.diagraminterchange.AUml1SemanticModelBridgeElement getAUml1SemanticModelBridgeElement();
    /**
     * Returns ADiagramOwner association proxy object.
     * @return ADiagramOwner association proxy object.
     */
    public org.omg.uml.diagraminterchange.ADiagramOwner getADiagramOwner();
    /**
     * Creates an instance of Point structure type.
     * @param x 
     * @param y 
     * @return Value of Point.
     */
    public org.omg.uml.diagraminterchange.Point createPoint(double x, double y);
    /**
     * Creates an instance of Dimension structure type.
     * @param width 
     * @param height 
     * @return Value of Dimension.
     */
    public org.omg.uml.diagraminterchange.Dimension createDimension(double width, double height);
    /**
     * Creates an instance of BezierPoint structure type.
     * @param base 
     * @param control1 
     * @param control2 
     * @return Value of BezierPoint.
     */
    public org.omg.uml.diagraminterchange.BezierPoint createBezierPoint(org.omg.uml.diagraminterchange.Point base, org.omg.uml.diagraminterchange.Point control1, org.omg.uml.diagraminterchange.Point control2);
}
