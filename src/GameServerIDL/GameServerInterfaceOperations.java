package GameServerIDL;

/**
 * Interface definition: GameServerInterface.
 * 
 * @author OpenORB Compiler
 */
public interface GameServerInterfaceOperations
{
    /**
     * Operation createPlayerAccount
     */
    public String createPlayerAccount(String firstName, String lastName, int age, String userName, String password, String ipAdd);

    /**
     * Operation processSignIn
     */
    public String processSignIn(String userName, String password, String ipAdd);

    /**
     * Operation processSignOut
     */
    public String processSignOut(String userName, String ipAdd);

    /**
     * Operation getPlayerStatus
     */
    public String getPlayerStatus(String ipAdd);

    /**
     * Operation suspendAccount
     */
    public String suspendAccount(String adminUnm, String adminPwd, String ipAdd, String username);

    /**
     * Operation transferAccount
     */
    public String transferAccount(String userName, String password, String oldipApp, String newiAdd);

}
