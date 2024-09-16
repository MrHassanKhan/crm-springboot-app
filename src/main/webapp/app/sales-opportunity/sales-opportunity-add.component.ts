import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { SalesOpportunityService } from 'app/sales-opportunity/sales-opportunity.service';
import { SalesOpportunityDTO } from 'app/sales-opportunity/sales-opportunity.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-sales-opportunity-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './sales-opportunity-add.component.html'
})
export class SalesOpportunityAddComponent implements OnInit {

  salesOpportunityService = inject(SalesOpportunityService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  leadValues?: Map<number,string>;
  assignedToValues?: Map<number,string>;

  addForm = new FormGroup({
    title: new FormControl(null, [Validators.maxLength(255)]),
    description: new FormControl(null, [Validators.maxLength(255)]),
    stage: new FormControl(null),
    value: new FormControl(null, [Validators.maxLength(255)]),
    lead: new FormControl(null),
    assignedTo: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@salesOpportunity.create.success:Sales Opportunity was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.salesOpportunityService.getLeadValues()
        .subscribe({
          next: (data) => this.leadValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.salesOpportunityService.getAssignedToValues()
        .subscribe({
          next: (data) => this.assignedToValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new SalesOpportunityDTO(this.addForm.value);
    this.salesOpportunityService.createSalesOpportunity(data)
        .subscribe({
          next: () => this.router.navigate(['/salesOpportunities'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
