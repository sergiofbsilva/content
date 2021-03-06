<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="context" type="pt.ist.bennu.core.presentationTier.Context" name="_CONTEXT_"/>

<logic:present name="context" property="selectedNode">
	<script type="text/javascript" src="<%= request.getContextPath() +"/javaScript/jquery.ui.draggable.js"%>"></script>
	<script type="text/javascript" src="<%= request.getContextPath() +"/javaScript/jquery.alerts.js"%>"></script>
	<script type="text/javascript" src="<%= request.getContextPath() +"/javaScript/alertHandlers.js"%>"></script>
	<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/dragContents.js" %>"></script>
	
	<bean:define id="selectedNode" name="context" property="selectedNode"/>
	<bean:define id="selectedPage" name="selectedNode" property="page"/>
	<h2><bean:write name="selectedPage" property="title"/></h2>
	<%
		final Object o = request.getAttribute("reorderSections");
		final boolean reorder = o != null && ((Boolean) o).booleanValue();
	%>
	<% if (reorder) {%>
		<div id="mainContainer">
			<div id="dragableElementsParentBox">
	<% } %>
			<logic:iterate id="section" name="selectedPage" property="orderedSections" indexId="sindex">
				<bean:define id="articleId">article<%= sindex %></bean:define>
				<div <% if (reorder) {%>dragableBox="true"<% } %> id="<%= articleId %>">
					<div class="contentStructuredP">
						<logic:present name="section" property="title">
							<logic:notEmpty name="section" property="title">
								<bean:define id="title" name="section" property="title"/>
								<logic:present name="USER_SESSION_ATTRIBUTE">
								<logic:present role="pt.ist.bennu.core.domain.RoleType.MANAGER">
									<div style="float: right;">
										<html:link page="/content.do?method=prepareEditSection" paramId="sectionOid" paramName="section" paramProperty="externalId">
											<bean:message bundle="MYORG_RESOURCES" key="label.content.section.edit"/>
										</html:link>
										|
										<html:link page="/content.do?method=reorderSections" paramId="nodeOid" paramName="selectedNode" paramProperty="externalId">
											<bean:message bundle="MYORG_RESOURCES" key="label.content.section.order.change"/>
										</html:link>
										|
										<bean:define id="confirmDelete"><bean:message
												bundle="MYORG_RESOURCES" key="label.content.section.delete.confirm"
												arg0="<%= title.toString() %>"/></bean:define>
										<bean:define id="sectionOID" name="section" property="externalId" type="java.lang.String"/>
										<html:link styleId='<%= "delete-section-" +  sectionOID %>' page="/content.do?method=deleteSection" paramId="sectionOid" paramName="section" paramProperty="externalId">
											<bean:message bundle="MYORG_RESOURCES" key="label.content.section.delete"/>
										</html:link>
										<script type="text/javascript">
											<%= "linkConfirmationHook('delete-section-" +  sectionOID + "','" + confirmDelete + "', '');" %>
										</script>
									</div>
								</logic:present>
								</logic:present>
								<logic:present name="section" property="title">
									<logic:present name="section" property="title.content">
										<h3>
											<bean:write name="section" property="title" filter="false"/>
										</h3>
									</logic:present>
								</logic:present>
							</logic:notEmpty>
						</logic:present>
						<bean:write name="section" property="contents" filter="false"/>
					</div>
				</div>
			</logic:iterate>
	<% if (reorder) {%>
			<div class="clear" id="clear"></div>
		</div>
		<logic:notEmpty name="selectedPage" property="sections">
			<form action="<%= request.getContextPath() %>/content.do" method="post">
				<input type="hidden" name="method" value="saveSectionOrders"/>
				<input type="hidden" id="articleOrders" name="articleOrders"/>
				<bean:define id="nodeOid" name="selectedNode" property="externalId" type="java.lang.String"/>
				<input type="hidden" name="nodeOid" value="<%= nodeOid %>"/>
				<bean:define id="originalArticleIds"><logic:iterate id="section" name="selectedPage" property="orderedSections" indexId="sindex"><% if (sindex > 0) {%>;<% } %><bean:write name="section" property="externalId"/></logic:iterate></bean:define>
				<input type="hidden" name="originalArticleIds" value="<%= originalArticleIds %>"/>
				<bean:define id="buttonLabel"><bean:message bundle="MYORG_RESOURCES" key="label.content.section.order.save"/></bean:define>
				<input type="submit" value="<%= buttonLabel %>" onclick="saveArticleOrders();"/>
			</form>
		</logic:notEmpty>
	</div>
	<div id="insertionMarker">
		<img src="<%= request.getContextPath() %>/CSS/marker_top.gif" alt=""/>
		<img src="<%= request.getContextPath() %>/CSS/marker_middle.gif" id="insertionMarkerLine" alt=""/>
		<img src="<%= request.getContextPath() %>/CSS/marker_bottom.gif" alt=""/>
	</div>
	<% } %>
</logic:present>
<logic:notPresent name="context" property="selectedNode">
	<h2>Welcome</h2>
	<p>
		This application has not yet been configured. To do so please log in with a user that has management priveledges, and start creating contents.
		Remember that you cad define which users have management roles in your build.properties file.
	</p>
</logic:notPresent>
	
