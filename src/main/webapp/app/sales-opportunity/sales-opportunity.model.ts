export class SalesOpportunityDTO {

  constructor(data:Partial<SalesOpportunityDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  title?: string|null;
  description?: string|null;
  stage?: string|null;
  value?: string|null;
  lead?: number|null;
  assignedTo?: number|null;

}
