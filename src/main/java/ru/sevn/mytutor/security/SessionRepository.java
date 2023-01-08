package ru.sevn.mytutor.security;

public interface SessionRepository<SESSION extends HasRememberMe> {
    SESSION findById(String cookieValue);
    void delete(SESSION s);
    default SESSION createAndSave (final String sessionId, final String login, final boolean remember, long timems) {
        var res = create(sessionId, login);
        if (remember) {
            res.setRememberMe (timems);
        }
        
        return save(res);
    }
    SESSION create (final String sessionId, final String login);
    SESSION save (SESSION s);
}
