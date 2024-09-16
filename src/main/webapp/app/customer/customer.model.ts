export class CustomerRequestDTO {

  constructor(data:Partial<CustomerRequestDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  firstName?: string|null;
  lastName?: string|null;
  email?: string|null;
  phone?: string|null;
  address?: string|null;
  // assignedSalesPerson?: number|null;

}

export class CustomerResponseDTO {

  constructor(data:Partial<CustomerResponseDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  firstName?: string|null;
  lastName?: string|null;
  email?: string|null;
  phone?: string|null;
  address?: string|null;
  assignedSalesPersonName?: number|null;

}
