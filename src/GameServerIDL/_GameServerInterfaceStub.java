package GameServerIDL;

/**
 * Interface definition: GameServerInterface.
 * 
 * @author OpenORB Compiler
 */
public class _GameServerInterfaceStub extends org.omg.CORBA.portable.ObjectImpl
        implements GameServerInterface
{
    static final String[] _ids_list =
    {
        "IDL:GameServerIDL/GameServerInterface:1.0"
    };

    public String[] _ids()
    {
     return _ids_list;
    }

    private final static Class _opsClass = GameServerIDL.GameServerInterfaceOperations.class;

    /**
     * Operation createPlayerAccount
     */
    public String createPlayerAccount(String firstName, String lastName, int age, String userName, String password, String ipAdd)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("createPlayerAccount",true);
                    _output.write_string(firstName);
                    _output.write_string(lastName);
                    _output.write_long(age);
                    _output.write_string(userName);
                    _output.write_string(password);
                    _output.write_string(ipAdd);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("createPlayerAccount",_opsClass);
                if (_so == null)
                   continue;
                GameServerIDL.GameServerInterfaceOperations _self = (GameServerIDL.GameServerInterfaceOperations) _so.servant;
                try
                {
                    return _self.createPlayerAccount( firstName,  lastName,  age,  userName,  password,  ipAdd);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation processSignIn
     */
    public String processSignIn(String userName, String password, String ipAdd)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("processSignIn",true);
                    _output.write_string(userName);
                    _output.write_string(password);
                    _output.write_string(ipAdd);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("processSignIn",_opsClass);
                if (_so == null)
                   continue;
                GameServerIDL.GameServerInterfaceOperations _self = (GameServerIDL.GameServerInterfaceOperations) _so.servant;
                try
                {
                    return _self.processSignIn( userName,  password,  ipAdd);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation processSignOut
     */
    public String processSignOut(String userName, String ipAdd)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("processSignOut",true);
                    _output.write_string(userName);
                    _output.write_string(ipAdd);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("processSignOut",_opsClass);
                if (_so == null)
                   continue;
                GameServerIDL.GameServerInterfaceOperations _self = (GameServerIDL.GameServerInterfaceOperations) _so.servant;
                try
                {
                    return _self.processSignOut( userName,  ipAdd);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation getPlayerStatus
     */
    public String getPlayerStatus(String ipAdd)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("getPlayerStatus",true);
                    _output.write_string(ipAdd);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getPlayerStatus",_opsClass);
                if (_so == null)
                   continue;
                GameServerIDL.GameServerInterfaceOperations _self = (GameServerIDL.GameServerInterfaceOperations) _so.servant;
                try
                {
                    return _self.getPlayerStatus( ipAdd);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation suspendAccount
     */
    public String suspendAccount(String adminUnm, String adminPwd, String ipAdd, String username)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("suspendAccount",true);
                    _output.write_string(adminUnm);
                    _output.write_string(adminPwd);
                    _output.write_string(ipAdd);
                    _output.write_string(username);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("suspendAccount",_opsClass);
                if (_so == null)
                   continue;
                GameServerIDL.GameServerInterfaceOperations _self = (GameServerIDL.GameServerInterfaceOperations) _so.servant;
                try
                {
                    return _self.suspendAccount( adminUnm,  adminPwd,  ipAdd,  username);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

    /**
     * Operation transferAccount
     */
    public String transferAccount(String userName, String password, String oldipApp, String newiAdd)
    {
        while(true)
        {
            if (!this._is_local())
            {
                org.omg.CORBA.portable.InputStream _input = null;
                try
                {
                    org.omg.CORBA.portable.OutputStream _output = this._request("transferAccount",true);
                    _output.write_string(userName);
                    _output.write_string(password);
                    _output.write_string(oldipApp);
                    _output.write_string(newiAdd);
                    _input = this._invoke(_output);
                    String _arg_ret = _input.read_string();
                    return _arg_ret;
                }
                catch(org.omg.CORBA.portable.RemarshalException _exception)
                {
                    continue;
                }
                catch(org.omg.CORBA.portable.ApplicationException _exception)
                {
                    String _exception_id = _exception.getId();
                    throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: "+ _exception_id);
                }
                finally
                {
                    this._releaseReply(_input);
                }
            }
            else
            {
                org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("transferAccount",_opsClass);
                if (_so == null)
                   continue;
                GameServerIDL.GameServerInterfaceOperations _self = (GameServerIDL.GameServerInterfaceOperations) _so.servant;
                try
                {
                    return _self.transferAccount( userName,  password,  oldipApp,  newiAdd);
                }
                finally
                {
                    _servant_postinvoke(_so);
                }
            }
        }
    }

}
