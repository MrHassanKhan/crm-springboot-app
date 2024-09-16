export class LeadDTO {

  constructor(data:Partial<LeadDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  firstname?: string|null;
  lastname?: string|null;
  email?: string|null;
  phone?: string|null;
  status?: string|null;
  assignedUserName?: string|null;
}
