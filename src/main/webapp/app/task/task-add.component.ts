import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { TaskService } from 'app/task/task.service';
import { TaskDTO } from 'app/task/task.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-task-add',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './task-add.component.html'
})
export class TaskAddComponent implements OnInit {

  taskService = inject(TaskService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  assignedToValues?: Map<number,string>;
  salesOpportunityValues?: Map<number,string>;

  addForm = new FormGroup({
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
      created: $localize`:@@task.create.success:Task was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
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
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new TaskDTO(this.addForm.value);
    this.taskService.createTask(data)
        .subscribe({
          next: () => this.router.navigate(['/tasks'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
