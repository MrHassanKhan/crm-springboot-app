export class ActivityLogDTO {

  constructor(data:Partial<ActivityLogDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  entityType?: string|null;
  entityId?: number|null;
  action?: string|null;
  description?: string|null;
  timestamp?: string|null;

}
