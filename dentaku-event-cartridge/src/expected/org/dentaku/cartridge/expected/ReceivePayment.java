/**
 * Generated file. Do not edit.
 */
package org.dentaku.cartridge.expected;

public abstract class ReceivePayment extends org.dentaku.foundation.event.AbstractEvent {
    public ReceivePayment() {
    }

    public abstract boolean execute() throws Exception;

    protected ReceivePayment(org.dentaku.foundation.output.OutputContext response) {
        super(response);
    }

    private org.dentaku.cartridge.expected.AccountsReceivable accountsRecievable;

    public org.dentaku.cartridge.expected.AccountsReceivable getAccountsRecievable() {
        return this.accountsRecievable;
    }

    public void setAccountsRecievable(org.dentaku.cartridge.expected.AccountsReceivable accountsRecievable) {
        this.accountsRecievable = accountsRecievable;
    }
    private org.dentaku.cartridge.expected.Check check;

    public org.dentaku.cartridge.expected.Check getCheck() {
        return this.check;
    }

    public void setCheck(org.dentaku.cartridge.expected.Check check) {
        this.check = check;
    }
}

