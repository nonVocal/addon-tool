package dev.nonvocal.gui.widget;

import javax.swing.*;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;


public class MarkdownPanel extends JEditorPane
{

    public MarkdownPanel()
    {
        setEditable(false);
        setContentType("text/html");
    }

    public void setMarkdown(String markdown)
    {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String render = renderer.render(document);

        setText(render);
    }
}
