/**
 * Copyright 2011 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 *
 */
package securesocial.provider;

/**
 * A class that provides the means to find save and create users
 * for the SecureSocial Module.
 *
 * @see DefaultUserService
 */
public class UserService {
    /**
     * This is the interface that defines the behaviour for UserService.
     * There is a default implementation in the DefaultUserService class that
     * stores things in a map.  This is just to provide an example, as a real
     * implementation should persist things in a database
     *
     * @see DefaultUserService
     */
    public interface Service {
        /**
         * Finds a SocialUser that matches the id
         *
         * @param id A UserId object
         * @return A SocialUser instance or null if no user matches the specified id.
         */
        SocialUser find(UserId id);

        /**
         * Finds a SocialUser that matches the given email
         *
         * @param email email address to search with
         * @return A SocialUser instance with the specified email, or null if none is found
         */
        SocialUser find(String email);

        /**
         * Saves the user in the backing store.
         *
         * @param user A SocialUser object
         */
        void save(SocialUser user);

        /**
         * Creates an activation request.  This is needed for users that
         * are creating an account in the system instead of using one in a 3rd party system.
         *
         * @param user The user that needs to be activated
         * @return A string with a uuid that will be embedded in the welcome email.
         */
        String createActivation(SocialUser user);

        /**
         * Activates a user by setting the isEmailVerified field to true.  This is only used
         * for UsernamePassword accounts.
         *
         * @param uuid The uuid created using the createActivation method.
         * @return Returns true if the user was activated - false otherwise.
         */
        boolean activate(String uuid);

        /**
         * Creates an password reset request.  This is needed for users with username/password
         * authentication that have lost their password, and need to set a new one
         *
         * @param user The user that needs to be reset the password
         * @return A string with a uuid that will be embedded in the password reset email.
         */
        String createPasswordReset(SocialUser user);

        /**
         * Return the SocialUser for this reset request, if such a request exists.
         *
         * @param username the username for this reset request
         * @param uuid     random uuid, i.e. the one-time-password for this password reset
         */
        SocialUser fetchForPasswordReset(String username, String uuid);

        /**
         * Disable the reset code for this user, once the password has been changed
         *
         * @param username the username for this reset request
         * @param uuid     random uuid, i.e. the one-time-password for this password reset
         */
        void disableResetCode(String username, String uuid);


        /**
         * This method deletes activations that were not completed by the user (The user did not follow the link
         * in the welcome email).
         * <p/>
         * The method should delete the information store for the user too.
         * store for the user.
         */
        void deletePendingActivations();
    }

    private static Service service;

    /**
     * Sets the Service implementation that will be used.
     *
     * @param delegate A Service instance.
     * @see securesocial.plugin.SecureSocialPlugin
     */
    public static void setService(Service delegate) {
        service = delegate;
    }

    /**
     * @see securesocial.provider.UserService.Service#find(UserId)
     */
    public static SocialUser find(UserId id) {
        checkIsInitialized();
        return service.find(id);
    }

    /**
     * @see securesocial.provider.UserService.Service#find(String)
     */
    public static SocialUser find(String email) {
        checkIsInitialized();
        return service.find(email);
    }

    private static void checkIsInitialized() {
        if (service == null) {
            throw new RuntimeException("UserService was not properly initialized.");
        }
    }

    /**
     * @see securesocial.provider.UserService.Service#save(SocialUser)
     */
    public static void save(SocialUser user) {
        checkIsInitialized();
        service.save(user);
    }

    /**
     * @see securesocial.provider.UserService.Service#createActivation(SocialUser)
     */
    public static String createActivation(SocialUser user) {
        checkIsInitialized();
        return service.createActivation(user);
    }

    /**
     * @see securesocial.provider.UserService.Service#activate(String)
     */
    public static boolean activate(String uuid) {
        checkIsInitialized();
        return service.activate(uuid);
    }

    /**
     * @see securesocial.provider.UserService.Service#createActivation(SocialUser)
     */
    public static String createPasswordReset(SocialUser user) {
        checkIsInitialized();
        return service.createPasswordReset(user);
    }

    /**
     * @see securesocial.provider.UserService.Service#fetchForPasswordReset(String, String)
     */
    public static SocialUser fetchForPasswordReset(String user, String uuid) {
        checkIsInitialized();
        return service.fetchForPasswordReset(user, uuid);
    }


    /**
     * @see securesocial.provider.UserService.Service#disableResetCode(String, String)
     */
    public static void disableResetCode(String username, String uuid) {
        checkIsInitialized();
        service.disableResetCode(username, uuid);
    }

    /**
     * @see securesocial.provider.UserService.Service#deletePendingActivations()
     */
    public static void deletePendingActivations() {
        checkIsInitialized();
        service.deletePendingActivations();
    }
}
