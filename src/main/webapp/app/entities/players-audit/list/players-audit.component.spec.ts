import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PlayersAuditService } from '../service/players-audit.service';

import { PlayersAuditComponent } from './players-audit.component';

describe('Component Tests', () => {
  describe('PlayersAudit Management Component', () => {
    let comp: PlayersAuditComponent;
    let fixture: ComponentFixture<PlayersAuditComponent>;
    let service: PlayersAuditService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlayersAuditComponent],
      })
        .overrideTemplate(PlayersAuditComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayersAuditComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PlayersAuditService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ transactionId: 'ABC' }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.playersAudits?.[0]).toEqual(jasmine.objectContaining({ transactionId: 'ABC' }));
    });
  });
});
