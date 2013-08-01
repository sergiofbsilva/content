/*
 * @(#)VersionedPageBean.java
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

import java.io.Serializable;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class VersionedPageBean implements Serializable {

    private MultiLanguageString title;
    private MultiLanguageString content;
    private boolean isMinor = false;

    public VersionedPageBean() {
    }

    public VersionedPageBean(VersionedPage page) {
        setTitle(page.getTitle());
        setContent(page.getCurrentContent());
    }

    public MultiLanguageString getContent() {
        return content;
    }

    public void setContent(MultiLanguageString content) {
        this.content = content;
    }

    public boolean isMinor() {
        return isMinor;
    }

    public void setMinor(boolean isMinor) {
        this.isMinor = isMinor;
    }

    public MultiLanguageString getTitle() {
        return title;
    }

    public void setTitle(MultiLanguageString title) {
        this.title = title;
    }

}
