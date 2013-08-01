/*
 * @(#)VersionedContent.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Content Module.
 *
 *   The Content Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Content Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Content Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.contents.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.contents.domain.PageVersion;
import module.contents.domain.VersionedFile;
import module.contents.domain.VersionedPage;
import module.contents.domain.VersionedPageBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.presentationTier.Context;
import pt.ist.bennu.core.presentationTier.DefaultContext;
import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;
import pt.ist.bennu.portal.Application;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.Atomic;

@Mapping(path = "/pageVersioning")
/**
 * 
 * @author Paulo Abrantes
 * 
 */
@Application(bundle = "resources/ContentResources", description = "option.create.new.versionedPage", path = "create",
        title = "option.create.new.versionedPage")
public class VersionedContent extends ContextBaseAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final ActionForward forward = super.execute(mapping, form, request, response);
        final DefaultContext layoutContext = (DefaultContext) getContext(request);
        layoutContext.addHead("/contents/head.jsp");
        layoutContext.addScript("/contents/scripts.jsp");
        return forward;

    }

    public final ActionForward app(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        return prepareCreateNewPage(mapping, form, request, response);
    }

    public final ActionForward prepareCreateNewPage(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        request.setAttribute("pageBean", new VersionedPageBean());
        final Context context = getContext(request);
        return context.forward("/contents/newVersionedPage.jsp");
    }

    public final ActionForward createNewPage(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final VersionedPageBean pageBean = getRenderedObject("pageBean");
        final VersionedPage versionedPage = VersionedPage.createNewPage(pageBean);
        return viewPageVersion(request, versionedPage.getCurrentVersion());
    }

    public final ActionForward viewPageVersion(HttpServletRequest request, PageVersion version) {
        request.setAttribute("version", version);
        final Context context = getContext(request);
        return context.forward("/contents/viewVersion.jsp");
    }

    public final ActionForward viewPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        VersionedPage page = getDomainObject(request, "pageId");
        return viewPageVersion(request, page.getCurrentVersion());
    }

    public final ActionForward prepareEditPage(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        VersionedPage page = getDomainObject(request, "pageId");
        VersionedPageBean bean = new VersionedPageBean(page);
        request.setAttribute("page", page);
        request.setAttribute("pageBean", bean);
        return forward(request, "/contents/editVersionedPage.jsp");
    }

    public final ActionForward editPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        VersionedPage page = getDomainObject(request, "pageId");
        VersionedPageBean bean = getRenderedObject("pageBean");
        page.edit(bean);
        return viewPageVersion(request, page.getCurrentVersion());
    }

    public final ActionForward recoverPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        VersionedPage page = getDomainObject(request, "pageId");
        page.recover();
        return viewPageVersion(request, page.getCurrentVersion());
    }

    public final ActionForward revertPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        VersionedPage page = getDomainObject(request, "pageId");
        PageVersion version = getDomainObject(request, "versionId");
        page.revertTo(version);
        return viewPageVersion(request, page.getCurrentVersion());
    }

    public final ActionForward viewVersion(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        PageVersion version = getDomainObject(request, "versionId");
        return viewPageVersion(request, version);
    }

    public final ActionForward addFile(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        PageVersion version = getDomainObject(request, "versionId");
        s1(version);
        return viewPageVersion(request, version);
    }

    @Atomic
    private void s1(PageVersion version) {
        version.getPage().addFiles(new VersionedFile());
    }

    public final ActionForward addFileVersion(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        VersionedFile file = getDomainObject(request, "versionFileId");
        PageVersion version = getDomainObject(request, "versionId");

        file.addVersion();
        return viewPageVersion(request, version);
    }

    public final ActionForward lockPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        PageVersion version = getDomainObject(request, "versionId");
        version.getPage().lock();

        return viewPageVersion(request, version);
    }

    public final ActionForward unlockPage(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        PageVersion version = getDomainObject(request, "versionId");
        version.getPage().unlock();

        return viewPageVersion(request, version);
    }

}
