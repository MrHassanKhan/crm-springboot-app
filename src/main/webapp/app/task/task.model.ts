export class TaskDTO {

  constructor(data:Partial<TaskDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  title?: string|null;
  description?: string|null;
  dueDate?: string|null;
  priority?: string|null;
  status?: string|null;
  assignedTo?: number|null;
  salesOpportunity?: number|null;

}
