import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { SalesOpportunityService } from 'app/sales-opportunity/sales-opportunity.service';
import { SalesOpportunityDTO } from 'app/sales-opportunity/sales-opportunity.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-sales-opportunity-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './sales-opportunity-edit.component.html'
})
export class SalesOpportunityEditComponent implements OnInit {

  salesOpportunityService = inject(SalesOpportunityService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  leadValues?: Map<number,string>;
  assignedToValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    title: new FormControl(null, [Validators.maxLength(255)]),
    description: new FormControl(null, [Validators.maxLength(255)]),
    stage: new FormControl(null),
    value: new FormControl(null, [Validators.maxLength(255)]),
    lead: new FormControl(null),
    assignedTo: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@salesOpportunity.update.success:Sales Opportunity was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
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
    this.salesOpportunityService.getSalesOpportunity(this.currentId!)
        .subscribe({
          next: (data) => updateForm(this.editForm, data),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.editForm.markAllAsTouched();
    if (!this.editForm.valid) {
      return;
    }
    const data = new SalesOpportunityDTO(this.editForm.value);
    this.salesOpportunityService.updateSalesOpportunity(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/salesOpportunities'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
