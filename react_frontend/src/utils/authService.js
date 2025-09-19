import { UserManager } from "oidc-client-ts";

const SSO_AUTHORITY = import.meta.env.VITE_SSO_AUTHORITY || "http://localhost:5556";

const baseUrl = `${window.location.origin}`;

const oidcConfig = {
  authority: `${SSO_AUTHORITY}`,
  client_id: "example-app",
  response_type: "code",
  scope: "openid profile email",
  redirect_uri: `${baseUrl}?callback=1`,
  post_logout_redirect_uri: `${baseUrl}`
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
