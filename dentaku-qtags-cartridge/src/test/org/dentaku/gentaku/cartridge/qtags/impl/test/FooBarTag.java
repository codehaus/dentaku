package org.dentaku.gentaku.cartridge.qtags.impl.test;

import org.dentaku.gentaku.GentakuTag;

/**
 * This tag is just a test tag
 *
 * @qtags.location method
 * @qtags.location constructor
 * @qtags.deprecated this tag is deprecated. use \@blurge instead
 * 
 * @author Aslak Helles&oslash;y
 * @author Konstantin Pribluda
 * @version $Revision$
 */
public interface FooBarTag extends GentakuTag {
    /**
     * Bla bla
     *
     * @qtags.allowed-value red
     * @qtags.allowed-value green
     * @qtags.default green
     */
    String getApple();

    /**
     * Ping pong
     *
     * @qtags.required
     */
    String getGrapeFruit();

    /**
     * Hip hop
     *
     * @qtags.default true
     */
    boolean isFunny();

    int getAge();
}
