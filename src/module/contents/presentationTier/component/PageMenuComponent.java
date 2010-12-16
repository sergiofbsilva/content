package module.contents.presentationTier.component;

import java.util.Collection;

import module.contents.domain.Page;
import module.contents.domain.Section;

import com.vaadin.ui.Label;

public class PageMenuComponent extends BaseComponent {

    private final transient Page page;

    public PageMenuComponent(final Page page) {
	this.page = page;
    }

    @Override
    public void attach() {
	super.attach();

	final StringBuilder stringBuilder = new StringBuilder();
	addMenu(stringBuilder, page.getOrderedSections());

	final Label content = new Label(stringBuilder.toString(), Label.CONTENT_XHTML);
	setCompositionRoot(content);
    }

    private void addMenu(final StringBuilder stringBuilder, final Collection<Section> sections) {
	if (!sections.isEmpty()) {
	    stringBuilder.append("<ol>");
	    for (final Section section : sections) {
		addMenu(stringBuilder, section);
	    }
	    stringBuilder.append("</ol>");
	}
    }

    private void addMenu(final StringBuilder stringBuilder, final Section section) {
	stringBuilder.append("<li>");
	htmlTag(stringBuilder, "a", section.getTitle().getContent(), "href", "#section" + section.getNumber());
	addMenu(stringBuilder, section.getOrderedSections());
	stringBuilder.append("</li>");
    }

}
