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

  // Data from the backend (signals so the page redraws when it arrives)
  incidents  = signal<Incident[]>([]);
  services   = signal<Service[]>([]);
  severities = signal<Severity[]>([]);
  engineers  = signal<Engineer[]>([]);

  // Form state
  form: IncidentRequest = this.emptyForm();
  editingId: number | null = null;   // null = creating, a number = editing that incident

  ngOnInit() {
    this.api.getServices().subscribe(data => this.services.set(data));
    this.api.getSeverities().subscribe(data => this.severities.set(data));
    this.api.getEngineers().subscribe(data => this.engineers.set(data));
    this.loadIncidents();
  }

  loadIncidents() {
    this.api.getIncidents().subscribe(data => this.incidents.set(data));
  }

  // ---------- Create / Update ----------

  save() {
    const request = this.editingId === null
      ? this.api.createIncident(this.form)
      : this.api.updateIncident(this.editingId, this.form);

    request.subscribe(() => {
      this.resetForm();
      this.loadIncidents();
    });
  }

  // Copy a row's values into the form and switch to edit mode
  edit(inc: Incident) {
    this.form = {
      title: inc.title,
      serviceId: inc.serviceId,
      severityId: inc.severityId,
      assigneeId: inc.assigneeId,
      status: inc.status,
    };
    this.editingId = inc.id;
    window.scrollTo(0, 0);   // the form is at the top; the table is long
  }

  resetForm() {
    this.form = this.emptyForm();
    this.editingId = null;
  }

  // ---------- Delete ----------

  remove(inc: Incident) {
    if (!confirm(`Delete incident #${inc.id}?`)) {
      return;
    }
    this.api.deleteIncident(inc.id).subscribe(() => {
      if (this.editingId === inc.id) {
        this.resetForm();   // don't leave a deleted incident in the form
      }
      this.loadIncidents();
    });
  }

  // ---------- Helpers ----------

  private emptyForm(): IncidentRequest {
    return { title: '', serviceId: null, severityId: null, assigneeId: null, status: 'open' };
  }

  // The table has ids; look up names from the dropdown data already loaded
  serviceName(id: number) {
    return this.services().find(s => s.id === id)?.name ?? '';
  }

  severityName(id: number) {
    return this.severities().find(sev => sev.id === id)?.name ?? '';
  }

  engineerName(id: number | null) {
    return this.engineers().find(e => e.id === id)?.name ?? 'Unassigned';
  }
}