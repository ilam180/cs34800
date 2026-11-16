import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Api } from '../api';
import { Engineer, Incident, IncidentRequest, Service, Severity } from '../models';

@Component({
  selector: 'app-incidents',
  imports: [DatePipe, FormsModule],
  templateUrl: './incidents.html',
})
export class Incidents implements OnInit {
  private api = inject(Api);

  // Data from the backend lives in signals, so the page re-renders when it arrives
  incidents  = signal<Incident[]>([]);
  services   = signal<Service[]>([]);
  severities = signal<Severity[]>([]);
  engineers  = signal<Engineer[]>([]);

  // The form's current values (two-way bound to the inputs with ngModel)
  form: IncidentRequest = { title: '', serviceId: null, severityId: null, assigneeId: null, status: 'open' };

  // Runs once when the page opens
  ngOnInit() {
    this.api.getServices().subscribe(data => this.services.set(data));
    this.api.getSeverities().subscribe(data => this.severities.set(data));
    this.api.getEngineers().subscribe(data => this.engineers.set(data));
    this.loadIncidents();
  }

  loadIncidents() {
    this.api.getIncidents().subscribe(data => this.incidents.set(data));
  }

  // The table has ids; look up the name from the dropdown data we already loaded
  serviceName(id: number) {
    return this.services().find(s => s.id === id)?.name ?? '';
  }

  save() {
    this.api.createIncident(this.form).subscribe(() => {
      this.loadIncidents();
      this.form = { title: '', serviceId: null, severityId: null, assigneeId: null, status: 'open' };
    });
  }
}