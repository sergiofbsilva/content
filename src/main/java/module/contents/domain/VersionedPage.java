/*
 * @(#)VersionedPage.java
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
package module.contents.domain;

import java.util.List;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.groups.Group;
import pt.ist.bennu.core.security.Authenticate;
import pt.ist.bennu.core.util.legacy.LegacyBundleUtil;
import pt.ist.bennu.portal.domain.MenuItem;
import pt.ist.dsi.commons.i18n.LocalizedString;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class VersionedPage extends VersionedPage_Base {

    public VersionedPage(MultiLanguageString title) {
        setMyOrg(MyOrg.getInstance());
        setTitle(title);
        createMenuItem(title);
        new PageVersion(this, 0);
    }

    private void createMenuItem(MultiLanguageString title) {
        MenuItem menu = new MenuItem();
        final LocalizedString titleLS = LegacyBundleUtil.getLocalizedString(title);
        menu.setTitle(titleLS);
        menu.setDescription(titleLS);
        menu.setAccessExpression("anyone");
        menu.setPath("pageVersioning.do?method=viewPage&pageId=" + getExternalId());
        Bennu.getInstance().getConfiguration().getMenu().addChild(menu);
    }

    @Atomic
    public static VersionedPage createNewPage(VersionedPageBean pageBean) {
        return new VersionedPage(pageBean.getTitle());
    }

    @Atomic
    public void edit(VersionedPageBean pageBean) {
        int currentVersionNumber = getCurrentVersionNumber();
        setTitle(pageBean.getTitle());
        new PageVersion(this, currentVersionNumber + 1, pageBean.getContent());
    }

    public int getCurrentVersionNumber() {
        return getCurrentVersion().getRevision();
    }

    public MultiLanguageString getCurrentContent() {
        return getCurrentVersion().getContent();
    }

    public User getVersionCreator() {
        return getCurrentVersion().getCreator();
    }

    @Atomic
    public void recover() {
        revertTo(getCurrentVersion());
    }

    public void revertTo(int version) {
        PageVersion pageVersion = getVersion(version);
        revertTo(pageVersion);
    }

    @Atomic
    public void revertTo(PageVersion pageVersion) {
        PageVersion currentVersion = getCurrentVersion();
        new PageVersion(this, currentVersion.getRevision() + 1, pageVersion.getContent());
    }

    public PageVersion getVersion(int revisionNumber) {
        for (PageVersion version : getPageVersions()) {
            if (version.getRevision() == revisionNumber) {
                return version;
            }
        }
        return null;
    }

    public List<FileVersion> getFilesForVersion(PageVersion pageVersion) {
        return pageVersion.getFiles();
    }

    @Override
    public void setLockPage(Boolean lockPage) {
        throw new UnsupportedOperationException("error cannot use setLockPage() use lock and unlock methods inteads");
    }

    @Atomic
    public void lock() {
        super.setLockPage(Boolean.TRUE);
    }

    @Atomic
    public void unlock() {
        super.setLockPage(Boolean.FALSE);
    }

    public boolean isLocked() {
        Boolean pageLocked = getLockPage();
        return pageLocked != null && pageLocked;
    }

    public boolean isUserAbleToViewOptions(User user) {
        return !isLocked() || (user != null && Group.parse("#managers").isMember(user));
    }

    public boolean isCurrentUserAbleToViewOptions() {
        return isUserAbleToViewOptions(Authenticate.getUser());
    }

    @Deprecated
    public java.util.Set<module.contents.domain.PageVersion> getPageVersions() {
        return getPageVersionsSet();
    }

    @Deprecated
    public java.util.Set<module.contents.domain.VersionedFile> getFiles() {
        return getFilesSet();
    }

}
