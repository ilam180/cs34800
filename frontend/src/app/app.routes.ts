import { Routes } from '@angular/router';
import { Incidents } from './incidents/incidents';
import { Report } from './report/report';

export const routes: Routes = [
  { path: 'incidents', component: Incidents },
  { path: 'report', component: Report },
  { path: '', redirectTo: 'incidents', pathMatch: 'full' },
];