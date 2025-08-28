import { UserManager } from "oidc-client-ts";

const oidcConfig = {
  authority: "http://localhost:5556",
  client_id: "example-app",
  redirect_uri: "http://localhost:5173/callback",
  response_type: "code",
  scope: "openid profile email",
  post_logout_redirect_uri: "http://localhost:5173",
};

export const userManager = new UserManager(oidcConfig);

export function login() {
  return userManager.signinRedirect();
}

export function handleCallback() {
  return userManager.signinRedirectCallback();
}

export function logout() {
  return userManager.signoutRedirect();
}

export function getUser() {
  return userManager.getUser();
}
