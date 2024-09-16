export class AuthenticationRequest {

  constructor(data:Partial<AuthenticationRequest>) {
    Object.assign(this, data);
  }

  username?: string|null;
  password?: string|null;

}

export class AuthenticationResponse {
  accessToken?: string;
}

export class RegistrationRequest {

  constructor(data:Partial<RegistrationRequest>) {
    Object.assign(this, data);
  }

  username?: string|null;
  email?: string|null;
  password?: string|null;
  fullname?: string|null;
  address?: string|null;
  accountType?: string|null;

}
