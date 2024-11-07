package com.github.terefang.jmelange.ldap;

import com.github.terefang.jmelange.ssl.ClientSSLSocketFactory;
import lombok.Data;
import lombok.SneakyThrows;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;
import javax.net.ssl.*;
import java.util.*;

@Data
public class JLDAP
{
    int timeout = 5000;
    
    String url;
    
    String authTemplate;
    String username;
    String password;
    private InitialLdapContext ctx;
    
    public static JLDAP create(String _url, String _user, String _pwd)
    {
        JLDAP _l = create(_url);
        return _l.bind(_user, _pwd) ? _l : null;
    }
    
    public static JLDAP create(String _url)
    {
        JLDAP _l = new JLDAP();
        _l.connect(_url);
        return _l;
    }
    
    @SneakyThrows
    public void connect(String _url, int _timeout)
    {
        this.setUrl(_url);
        this.setTimeout(_timeout);
    }
    
    public void connect(String _url)
    {
        this.connect(_url, this.timeout);
    }
    
    public boolean bind(String _user, String _pass)
    {
        this.setUsername(_user);
        this.setPassword(_pass);
        return this.bind();
    }
    
    @SneakyThrows
    public boolean bind()
    {
        Hashtable env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        
        if(this.timeout>0)
        {
            env.put("com.sun.jndi.ldap.connect.timeout", Objects.toString(this.timeout));
            env.put("com.sun.jndi.ldap.read.timeout", Objects.toString(this.timeout));
        }
        
        env.put(Context.PROVIDER_URL, this.url);
        
        // Create initial context
        this.ctx = new InitialLdapContext(env, null);
        
        if(this.url.startsWith("ldaps:")) {
            env.put("java.naming.ldap.factory.socket", ClientSSLSocketFactory.class.getCanonicalName());
        }
        else
        {
            try
            {
                StartTlsResponse tls = (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());
                tls.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                tls.negotiate(ClientSSLSocketFactory.create());
            }
            catch (Exception _xe)
            {
                ctx = new InitialLdapContext(env, null);
            }
        }
        
        if(this.username!=null)
        {
            // Perform simple client authentication
            ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, this.username);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, this.password);
        }

        try
        {
            ctx.getAttributes("");
            return true;
        }
        catch (Exception _xe) { }
        return false;
    }
    
    @SneakyThrows
    public boolean authenticate(String _user, String _pass)
    {
        if(this.authTemplate!=null)
        {
            _user = String.format(this.authTemplate, _user);
        }
        return authenticateDN(_user,_pass);
    }
    
    @SneakyThrows
    public boolean authenticate(String _tmpl, String _user, String _pass)
    {
        if(_tmpl!=null)
        {
            _user = String.format(_tmpl, _user);
        }
        return authenticateDN(_user,_pass);
    }
    
    @SneakyThrows
    public boolean authenticateDN(String _dn, String _pass)
    {
        this.ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
        this.ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, _dn);
        this.ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, _pass);
        try
        {
            this.ctx.getAttributes("");
            return true;
        }
        catch (Exception _xe) { }
        return false;
    }
    
    @SneakyThrows
    public void close()
    {
        if(this.ctx!=null)
            this.ctx.close();
    }
    
    @SneakyThrows
    public void diconnect()
    {
        if(this.ctx!=null)
            this.ctx.close();
    }
    
    @SneakyThrows
    public List<Map<String, String[]>> _search(String _base, String _filter, String _scope, String... _attr)
    {
        SearchControls _sc = new SearchControls();
        if("sub".equalsIgnoreCase(_scope)) {
            _sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        } else if("one".equalsIgnoreCase(_scope)) {
            _sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        } else if("base".equalsIgnoreCase(_scope)) {
            _sc.setSearchScope(SearchControls.OBJECT_SCOPE);
        }
        
        if(_attr!=null && _attr.length>0)
        {
            _sc.setReturningAttributes(_attr);
        }
        
        NamingEnumeration<SearchResult> _list = this.ctx.search(_base, _filter, _sc );
        return convertEnum(_list);
    }
    
    @SneakyThrows
    public Map<String, String[]> _get(String _base,String... _attr)
    {
        SearchControls _sc = new SearchControls();
        _sc.setSearchScope(SearchControls.OBJECT_SCOPE);
        
        if(_attr!=null && _attr.length>0)
        {
            _sc.setReturningAttributes(_attr);
        }
        
        Attributes _entry = this.ctx.getAttributes(_base, _attr);
        return convertEntry(_entry);
    }
    
    @SneakyThrows
    static List<Map<String, String[]>> convertEnum(NamingEnumeration<SearchResult> _list)
    {
        List<Map<String, String[]>> _ret = new Vector<>();
        while(_list.hasMore())
        {
            Map<String, String[]> _row = convertEntry(_list.next().getAttributes());
            _ret.add(_row);
        }
        return _ret;
    }
    
    @SneakyThrows
    static Map<String, String[]> convertEntry(Attributes _entry)
    {
        Map<String, String[]> _row = new LinkedHashMap<>();
        NamingEnumeration<? extends Attribute> _ae = _entry.getAll();
        while(_ae.hasMore())
        {
            Attribute _ar = _ae.next();
            String _key = _ar.getID();
            String[] _val = new String[_ar.size()];
            for(int _i =0; _i<_val.length; _i++)
            {
                _val[_i] = Objects.toString(_ar.get(_i));
            }
            _row.put(_key,_val);
        }
        return _row;
    }

    public List<Map<String, String[]>> search(String baseDN, String _filter, String... attributes)
    {
        return _search(baseDN,_filter,"sub", attributes);
    }
    
    public List<Map<String, String[]>> search(String baseDN, String _filter, List<String> attributes)
    {
        return _search(baseDN,_filter,"sub", (String[]) attributes.toArray(new String[attributes.size()]));
    }
    
    public List<Map<String, String[]>> search(String baseDN, String _filter, String attr1)
    {
        return _search(baseDN,_filter,"sub", attr1);
    }
    
    public List<Map<String, String[]>> search(String baseDN, String _filter, String attr1, String attr2)
    {
        return _search(baseDN,_filter,"sub", attr1, attr2);
    }
    
    public List<Map<String, String[]>> search(String baseDN, String _filter, String attr1, String attr2, String attr3)
    {
        return _search(baseDN,_filter,"sub", attr1, attr2, attr3);
    }
    
    public List<Map<String, String[]>> search(String baseDN, String _filter, String attr1, String attr2, String attr3, String attr4)
    {
        return _search(baseDN,_filter,"sub", attr1, attr2, attr3, attr4);
    }
    
    public List<Map<String, String[]>> search(String baseDN, String _filter, String attr1, String attr2, String attr3, String attr4, String attr5)
    {
        return _search(baseDN,_filter,"sub", attr1, attr2, attr3, attr4, attr5);
    }
    
    public List<Map<String, String[]>> search(String baseDN, String _filter)
    {
        return _search(baseDN,_filter,"sub");
    }
    
    public Map<String, String[]> get(String dn, String... attributes)
    {
        return _get(dn, attributes);
    }
    
    public Map<String, String[]> get(String dn, String attr1)
    {
        return _get(dn, attr1);
    }
    
    public Map<String, String[]> get(String dn, String attr1, String attr2)
    {
        return _get(dn, attr1, attr2);
    }
    
    public Map<String, String[]> get(String dn, String attr1, String attr2, String attr3)
    {
        return _get(dn, attr1, attr2, attr3);
    }
    
    public Map<String, String[]> get(String dn, String attr1, String attr2, String attr3, String attr4)
    {
        return _get(dn, attr1, attr2, attr3, attr4);
    }
    
    public Map<String, String[]> get(String dn, String attr1, String attr2, String attr3, String attr4, String attr5)
    {
        return _get(dn, attr1, attr2, attr3, attr4, attr5);
    }
    
    public Map<String, String[]> get(String dn, List<String> attributes)
    {
        return _get(dn, (String[]) attributes.toArray(new String[attributes.size()]));
    }
    
    public Map<String, String[]> get(String dn)
    {
        return _get(dn);
    }
    
    public JLDAP()
    {
    }
    
}