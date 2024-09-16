export class UserDTO {

  constructor(data:Partial<UserDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  username?: string|null;
  email?: string|null;
  password?: string|null;
  fullname?: string|null;
  address?: string|null;
  accountType?: string|null;

}
