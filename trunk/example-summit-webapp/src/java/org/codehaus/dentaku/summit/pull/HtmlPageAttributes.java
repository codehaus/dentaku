/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.dentaku.summit.pull;

import org.codehaus.plexus.summit.pull.RequestTool;
import org.codehaus.plexus.summit.rundata.RunData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

/**
 * Template context tool that can be used to set various attributes of a
 * HTML page.  This tool does not automatically make the changes in the HTML
 * page for you.  You must use this tool in your layout template to retrieve
 * the attributes.
 * <p>
 * The set/add methods are can be used from a screen template, action, screen
 * class, layour template, or anywhere else.  The get methods should be used in
 * your layout template(s) to construct the appropriate HTML tags.
 *<p>
 * Example usage of this tool to build the HEAD and BODY tags in your layout
 * templates:
 * <p>
 *  <code>
 *  ## Set defaults for all pages using this layout.  Anything set here can<br>
 *  ## be overridden in the screen template.<br>
 *  $page.setTitle("My default page title");<br>
 *  $page.setHttpEquiv("Content-Style-Type","text/css")<br>
 *  $page.addStyleSheet($content.getURI("myStyleSheet.css"))<br>
 *  $page.addScript($content.getURI("globalJavascriptCode.js"))<br>
 *  <br>
 *  ## build the HTML, HEAD, and BODY tags dynamically<br>
 *  &lt;html&gt;<br>
 *    &lt;head&gt;<br>
 *      #if( $page.Title != "" )<br>
 *      &lt;title&gt;$page.Title&lt;/title&gt;<br>
 *      #end<br>
 *      #foreach($metaTag in $page.MetaTags.keySet())<br>
 *      &lt;meta name="$metaTag" content="$page.MetaTags.get($metaTag)"&gt;<br>
 *      #end<br>
 *      #foreach($httpEquiv in $page.HttpEquivs.keySet())<br>
 *      &lt;meta http-equiv="$httpEquiv" content="$page.HttpEquivs.get($httpEquiv)"&gt;<br>
 *      #end<br>
 *      #foreach( $styleSheet in $page.StyleSheets )<br>
 *        &lt;link rel="stylesheet" href="$styleSheet.Url"<br>
 *          #if($styleSheet.Type != "" ) type="$styleSheet.Type" #end<br>
 *          #if($styleSheet.Media != "") media="$styleSheet.Media" #end<br>
 *          #if($styleSheet.Title != "") title="$styleSheet.Title" #end<br>
 *        &gt;<br>
 *      #end<br>
 *      #foreach( $script in $page.Scripts )<br>
 *        &lt;script type="text/javascript" src="$script" language="JavaScript"&gt;&lt;/script&gt;<br>
 *      #end<br>
 *    &lt;/head&gt;<br>
 *<br>
 *    ## Construct the body tag.  Iterate through the body attributes to build the opening tag<br>
 *    &lt;body<br>
 *      #foreach( $attributeName in $page.BodyAttributes.keySet() )<br>
 *        $attributeName = "$page.BodyAttributes.get($attributeName)"<br>
 *      #end<br>
 *     &gt;
 * </code>
 * <p>
 * Example usages of this tool in your screen templates:<br>
 *   <code>$page.addScript($content.getURI("myJavascript.js")<br>
 *   $page.setTitle("My page title")<br>
 *   $page.setHttpEquiv("refresh","5; URL=http://localhost/nextpage.html")</code>
 *
 * @author <a href="mailto:quintonm@bellsouth.net">Quinton McCombs</a>
 * @author <a href="mailto:seade@backstagetech.com.au">Scott Eade</a>
 * @version $Id$
 */

public class HtmlPageAttributes implements RequestTool {

    /** The title */
    private String title;
    /** Body Attributes */
    private Map bodyAttributes = new HashMap();
    /** Script references */
    private List scripts = new ArrayList();
    /** Stylesheet references */
    private List styleSheets = new ArrayList();
    /** Inline styles */
    private List styles = new ArrayList();
    /** Meta tags for the HEAD */
    private Map metaTags = new HashMap();
    /** http-equiv tags */
    private Map httpEquivs = new HashMap();
    /** Doctype */
    private String doctype = null;
    /** The RunData object. */
    private RunData data = null;
	private String identifier;
	private String uri;
	private String tag;

    /**
     * Default constructor. The init method must be called before use
     */
    public HtmlPageAttributes()
    {
    }

    /**
     * Construct a new instance with the given RunData object.
     *
     * @param data a RunData instance
     */
    public HtmlPageAttributes(RunData data)
    {
        init(data);
    }

    /**
     * Initialise this instance with the given RunData object.
     * (ApplicationTool method)
     *
     * @param data Assumed to be a RunData instance
     */
    public void init(Object data)
    {
        this.title = null;
        this.bodyAttributes.clear();
        this.scripts.clear();
        this.styleSheets.clear();
        this.styles.clear();
        this.metaTags.clear();
        this.httpEquivs.clear();
    }

    /**
     * Refresh method - does nothing
     */
    public void refresh()
    {
        // empty
    }

    /**
     * Set the title in the page.  This returns an empty String so
     * that the template doesn't complain about getting a null return
     * value.  Subsequent calls to this method will replace the current
     * title.
     *
     * @param title A String with the title.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes setTitle(String title)
    {
        this.title = title;
        return this;
    }

    /**
     * Get the title in the page.  This returns an empty String if
     * empty so that the template doesn't complain about getting a null
     * return value.
     *
     * @return A String with the title.
     */
    public String getTitle()
    {
        if (StringUtils.isEmpty(this.title))
        {
            return "";
        }
        return title;
    }

    /**
     * Adds an attribute to the BODY tag.
     *
     * @param name A String.
     * @param value A String.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes addBodyAttribute(String name, String value)
    {
        this.bodyAttributes.put(name, value);
        return this;
    }

    /**
     * Returns the map of body attributes
     *
     * @return the map
     */
    public Map getBodyAttributes()
    {
        return this.bodyAttributes;
    }

    /**
     * Adds a script reference
     *
     * @param scriptURL
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes addScript(String scriptURL)
    {
        this.scripts.add(scriptURL);
        return this;
    }

    /**
     * Returns a collection of script URLs
     *
     * @return list of String objects constainings URLs of javascript files
     * to include
     */
    public List getScripts()
    {
        return this.scripts;
    }

    /**
     * Adds a style sheet reference
     *
     * @param styleSheetURL URL of the style sheet
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes addStyleSheet(String styleSheetURL)
    {
        addStyleSheet(styleSheetURL, "screen", null, "text/css");
        return this;
    }

    /**
     * Adds a style sheet reference
     *
     * @param styleSheetURL URL of the style sheet
     * @param media name of the media
     * @param title title of the stylesheet
     * @param type content type
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes addStyleSheet(String styleSheetURL,
                                            String media, String title, String type)
    {
        StyleSheet ss = new StyleSheet(styleSheetURL);
        ss.setMedia(media);
        ss.setTitle(title);
        ss.setType(type);
        this.styleSheets.add(ss);
        return this;
    }

    /**
     * Returns a collection of script URLs
     *
     * @return list StyleSheet objects (inner class)
     */
    public List getStyleSheets()
    {
        return this.styleSheets;
    }

    /**
     * Adds a STYLE element to the HEAD of the page with the provided content.
     *
     * @param styleText The contents of the <code>style</code> tag.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes addStyle(String styleText)
    {
        this.styles.add(styleText);
        return this;
    }

    /**
     * Returns a collection of styles
     *
     * @return list of String objects containing the contents of style tags
     */
    public List getStyles()
    {
        return this.styles;
    }

    /**
     * Set a keywords META tag in the HEAD of the page.
     *
     * @param keywords A String.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes setKeywords(String keywords)
    {
        this.metaTags.put("keywords", keywords);
        return this;
    }

    /**
     * Sets a HttpEquiv META tag in the HEAD of the page, usage:
     * <br><code>setHttpEquiv("refresh", "5; URL=http://localhost/nextpage.html")</code>
     * <br><code>setHttpEquiv("Expires", "Tue, 20 Aug 1996 14:25:27 GMT")</code>
     *
     * @param httpEquiv The value to use for the http-equiv attribute.
     * @param content   The text for the content attribute of the meta tag.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes setHttpEquiv(String httpEquiv, String content)
    {
        this.httpEquivs.put(httpEquiv, content);
        return this;
    }

    /**
     * Add a description META tag to the HEAD of the page.
     *
     * @param description A String.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes setDescription(String description)
    {
        this.metaTags.put("description", description);
        return this;
    }

    /**
     * Set the background image for the BODY tag.
     *
     * @param url A String.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes setBackground(String url)
    {
        this.bodyAttributes.put("background", url);
        return this;
    }

    /**
     * Set the background color for the BODY tag.  You can use either
     * color names or color values (e.g. "white" or "#ffffff" or
     * "ffffff").
     *
     * @param color A String.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes setBgColor(String color)
    {
        this.bodyAttributes.put("BGCOLOR", color);
        return this;
    }

    /**
     * Set the text color for the BODY tag.  You can use either color
     * names or color values (e.g. "white" or "#ffffff" or "ffffff").
     *
     * @param color A String.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes setTextColor(String color)
    {
        this.bodyAttributes.put("TEXT", color);
        return this;
    }

    /**
     * Set the link color for the BODY tag.  You can use either color
     * names or color values (e.g. "white" or "#ffffff" or "ffffff").
     *
     * @param color A String.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes setLinkColor(String color)
    {
        this.bodyAttributes.put("LINK", color);
        return this;
    }

    /**
     * Set the visited link color for the BODY tag.
     *
     * @param color A String.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes setVlinkColor(String color)
    {
        this.bodyAttributes.put("VLINK", color);
        return this;
    }

    /**
     * Set the active link color for the BODY tag.
     *
     * @param color A String.
     * @return a <code>HtmlPageAttributes</code> (self).
     */
    public HtmlPageAttributes setAlinkColor(String color)
    {
        this.bodyAttributes.put("ALINK", color);
        return this;
    }

    /**
     * Gets the map of http equiv tags
     *
     * @return Map of http equiv names to the contents
     */
    public Map getHttpEquivs()
    {
        return this.httpEquivs;
    }

    /**
     * Gets the map of meta tags
     *
     * @return Map of http equiv names to the contents
     */
    public Map getMetaTags()
    {
        return this.metaTags;
    }

    /**
     * A dummy toString method that returns an empty string.
     *
     * @return An empty String ("").
     */
    public String toString()
    {
        return "";
    }

    /**
     * Helper class to hold data about a stylesheet
     */
    public class StyleSheet
    {
        private String url;
        private String title;
        private String media;
        private String type;

        /**
         * Constructor requiring the URL to be set
         *
         * @param url URL of the external style sheet
         */
        public StyleSheet(String url)
        {
            setUrl(url);
        }

        /**
         * Gets the content type of the style sheet
         *
         * @return content type
         */
        public String getType()
        {
            return (StringUtils.isEmpty(type) ? "" : type);
        }

        /**
         * Sets the content type of the style sheet
         *
         * @param type content type
         */
        public void setType(String type)
        {
            this.type = type;
        }

        /**
         * @return String representation of the URL
         */
        public String getUrl()
        {
            return url;
        }

        /**
         * Sets the URL of the external style sheet
         *
         * @param url The URL of the stylesheet
         */
        private void setUrl(String url)
        {
            this.url = url;
        }

        /**
         * Gets the title of the style sheet
         *
         * @return title
         */
        public String getTitle()
        {
            return (StringUtils.isEmpty(title) ? "" : title);
        }

        /**
         * Sets the title of the stylesheet
         *
         * @param title
         */
        public void setTitle(String title)
        {
            this.title = title;
        }

        /**
         * Gets the media for which the stylesheet should be applied.
         *
         * @return name of the media
         */
        public String getMedia()
        {
            return (StringUtils.isEmpty(media) ? "" : media);
        }

        /**
         * Sets the media for which the stylesheet should be applied.
         *
         * @param media name of the media
         */
        public void setMedia(String media)
        {
            this.media = media;
        }

    }
    
    /**
     * Retrieve the default Doctype as configured 
     * default.doctype.root.element, default.doctype.identifier and
     * default.doctype.url properties (defaults are "HTML", 
     * "-//W3C//DTD HTML 4.01 Transitional//EN" and 
     * "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd" respectively).
     * 
     * @return the DOCTYPE tag constructed from the config
     */
    public String getDefaultDoctype()
    {
        if (doctype == null)
        {
            if (StringUtils.isEmpty(tag))
            {
                doctype = "";
            }
            else
            {
                doctype = buildDoctype(tag, identifier, uri);
            }
        }
        return doctype;
    }
    
    /**
     * Build the doctype element.
     * 
     * @param tag the tag whose DTD is being declared.
     * @param identifier the identifier for the doctype declaration.
     * @param uri the uri for the doctype declaration.
     * @return the doctype.
     */
    private String buildDoctype(String tag, String identifier, String uri)
    {
        StringBuffer doctypeBuf = new StringBuffer("<!DOCTYPE ");
        doctypeBuf.append(tag);

        if (StringUtils.isNotEmpty(identifier))
        {
            doctypeBuf.append(" PUBLIC \"");
            doctypeBuf.append(identifier);
            doctypeBuf.append("\" \"");
        }
        else
        {
            doctypeBuf.append(" SYSTEM \"");
        }

        doctypeBuf.append(uri);
        doctypeBuf.append("\">");

        return doctypeBuf.toString();
    }

	/* (non-Javadoc)
	 * @see org.codehaus.plexus.summit.pull.RequestTool#setRunData(org.codehaus.plexus.summit.rundata.RunData)
	 */
	public void setRunData(RunData data) {
        // we blithely cast to RunData as the runtime error thrown
        // if data is null or not RunData is appropriate.
        this.data = data;

        // clear cached attributes
        this.title = null;
        this.bodyAttributes.clear();
        this.scripts.clear();
        this.styleSheets.clear();
        this.styles.clear();
        this.metaTags.clear();
        this.httpEquivs.clear();
	}
	/**
	 * @return Returns the identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	/**
	 * @return Returns the tag.
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag The tag to set.
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	/**
	 * @return Returns the uri.
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * @param uri The uri to set.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
}
