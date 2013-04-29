package de.agilecoders.wicket.requirejs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import de.agilecoders.wicket.WicketApplicationTest;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.TagTester;
import org.junit.Test;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public class RequireJsTagTest extends WicketApplicationTest {

    @Test
    public void correctScriptTagIsRendered() {
        tester().startComponentInPage(new RequireJsTag("id", Model.of("bundles!main.js")), newMarkup("id"));

        TagTester tag = tester().getTagByWicketId("id");

        assertThat(tag.getName(), is(equalTo("script")));
        assertThat(tag.getAttribute("type"), is(equalTo("application/javascript")));
    }


    /**
     * TODO
     *
     * @param id TODO
     * @return TODO
     */
    private IMarkupFragment newMarkup(final String id) {
        return Markup.of("<script wicket:id=\"" + id + "\"></script>");
    }


}
