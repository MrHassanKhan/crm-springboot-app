import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { SalesOpportunityService } from 'app/sales-opportunity/sales-opportunity.service';
import { SalesOpportunityDTO } from 'app/sales-opportunity/sales-opportunity.model';


@Component({
  selector: 'app-sales-opportunity-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './sales-opportunity-list.component.html'})
export class SalesOpportunityListComponent implements OnInit, OnDestroy {

  salesOpportunityService = inject(SalesOpportunityService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  salesOpportunities?: SalesOpportunityDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@salesOpportunity.delete.success:Sales Opportunity was removed successfully.`,
      'salesOpportunity.task.salesOpportunity.referenced': $localize`:@@salesOpportunity.task.salesOpportunity.referenced:This entity is still referenced by Task ${details?.id} via field Sales Opportunity.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.loadData();
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData();
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }
  
  loadData() {
    this.salesOpportunityService.getAllSalesOpportunities()
        .subscribe({
          next: (data) => this.salesOpportunities = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.salesOpportunityService.deleteSalesOpportunity(id)
          .subscribe({
            next: () => this.router.navigate(['/salesOpportunities'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/salesOpportunities'], {
                  state: {
                    msgError: this.getMessage(messageParts[0], { id: messageParts[1] })
                  }
                });
                return;
              }
              this.errorHandler.handleServerError(error.error)
            }
          });
    }
  }

}
