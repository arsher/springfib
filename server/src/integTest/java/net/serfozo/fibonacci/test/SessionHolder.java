package net.serfozo.fibonacci.test;

public final class SessionHolder{
    private SessionWrapper session;

    public SessionWrapper getSession() {
        return session;
    }

    public void setSession(SessionWrapper session) {
        this.session = session;
    }
}