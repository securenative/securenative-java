public class ActionSetTest {
    @BeforeClass
    public static void setup() {
    }

    @Test
    public void blockUserIpCountryForever() {
        String ip = "10.0.0.1";
        String user = "DC48C86C04DF0005FB4DE3629AB1F";
        String country = "US";

        actionset = new ActionSet("ActionTest")
        actionSet.add(SetType.IP.name(), ip);
        actionSet.add(SetType.USER.name(), user);
        actionSet.add(SetType.COUNTRY.name(), country);

        Assert.assertTrue(actionSet.has(SetType.IP.name(), ip));
        Assert.assertTrue(actionSet.has(SetType.USER.name(), user));
        Assert.assertTrue(actionSet.has(SetType.COUNTRY.name(), country));
    }

    @Test
    public void blockUserIpCountryTemp() {
        String ip = "10.0.0.1";
        String user = "DC48C86C04DF0005FB4DE3629AB1F";
        String country = "US";
        Long timeout = 2000;

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.add(SetType.IP.name(), ip, timeout);
        actionSet.add(SetType.USER.name(), user, timeout);
        actionSet.add(SetType.COUNTRY.name(), country, timeout);

        Thread.sleep(timeout + 100);

        Assert.assertFalse(actionSet.has(SetType.IP.name(), ip));
        Assert.assertFalse(actionSet.has(SetType.USER.name(), user));
        Assert.assertFalseactionSet.has(SetType.COUNTRY.name(), country));
    }

    @Test
    public void expireFirstIp() {
        String ip = "10.0.0.1";
        String ip2 = "10.0.0.2";
        Long timeout = 2000;

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.add(SetType.IP.name(), ip, 1000);
        actionSet.add(SetType.IP.name(), ip, 10000);

        Thread.sleep(timeout + 100);

        Assert.assertFalse(actionSet.has(SetType.IP.name(), ip));
        Assert.assertTrue(actionSet.has(SetType.IP.name(), ip2));
    }

    @Test
    public void expireFirstUser() {
        String user = "12345";
        String user2 = "333333";
        Long timeout = 2000;

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.add(SetType.USER.name(), user, 1000);
        actionSet.add(SetType.USER.name(), user2, 10000);

        Thread.sleep(timeout + 100);

        Assert.assertFalse(actionSet.has(SetType.USER.name(), user));
        Assert.assertTrue(actionSet.has(SetType.USER.name(), user2));
    }

    @Test
    public void blockIpRangeForever() {
        String ipRange = "2.3.4.5/32";
        String validIP = "2.3.4.5";
        String invalidIP = "1.2.3.4";

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.add(SetType.IP.name(), ipRange);

        Assert.assertTrue(actionSet.has(SetType.IP.name(), validIP));
        Assert.assertFalse(actionSet.has(SetType.IP.name(), invalidIP));
    }

    @Test
    public void ingoreInvalidIp() {
        String badIP = "2.3.45";

        ActionSet actionSet = new ActionSet("ActionTest");
        actionSet.add(SetType.IP.name(), badIP);

        Assert.assertFalse(actionSet.has(SetType.IP.name(), badIP));
    }

    @Test
    public void deleteNotExisted() {
        String ip = "10.0.0.1";
        String user = "DC48C86C04DF0005FB4DE3629AB1F";
        String country = "US";

        actionset = new ActionSet("ActionTest")
        actionSet.delete(SetType.IP.name(), ip);
        actionSet.delete(SetType.USER.name(), user);
        actionSet.delete(SetType.COUNTRY.name(), country);

        Assert.assertFalse(actionSet.has(SetType.IP.name(), ip));
        Assert.assertFalse(actionSet.has(SetType.USER.name(), user));
        Assert.assertFalse(actionSet.has(SetType.COUNTRY.name(), country));
    }
}