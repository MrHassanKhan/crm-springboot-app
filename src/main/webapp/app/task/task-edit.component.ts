import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { TaskService } from 'app/task/task.service';
import { TaskDTO } from 'app/task/task.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-task-edit',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './task-edit.component.html'
})
export class TaskEditComponent implements OnInit {

  taskService = inject(TaskService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  assignedToValues?: Map<number,string>;
  salesOpportunityValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    title: new FormControl(null, [Validators.maxLength(255)]),
    description: new FormControl(null, [Validators.maxLength(255)]),
    dueDate: new FormControl(null),
    priority: new FormControl(null),
    status: new FormControl(null),
    assignedTo: new FormControl(null),
    salesOpportunity: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@task.update.success:Task was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.taskService.getAssignedToValues()
        .subscribe({
          next: (data) => this.assignedToValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.taskService.getSalesOpportunityValues()
        .subscribe({
          next: (data) => this.salesOpportunityValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.taskService.getTask(this.currentId!)
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
    const data = new TaskDTO(this.editForm.value);
    this.taskService.updateTask(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/tasks'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
