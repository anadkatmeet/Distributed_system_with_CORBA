package GameServerIDL;

/**
 * Holder class for : GameServerInterface
 * 
 * @author OpenORB Compiler
 */
final public class GameServerInterfaceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal GameServerInterface value
     */
    public GameServerIDL.GameServerInterface value;

    /**
     * Default constructor
     */
    public GameServerInterfaceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public GameServerInterfaceHolder(GameServerIDL.GameServerInterface initial)
    {
        value = initial;
    }

    /**
     * Read GameServerInterface from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = GameServerInterfaceHelper.read(istream);
    }

    /**
     * Write GameServerInterface into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        GameServerInterfaceHelper.write(ostream,value);
    }

    /**
     * Return the GameServerInterface TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return GameServerInterfaceHelper.type();
    }

}
