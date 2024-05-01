import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Hoofddoel e2e test', () => {
  const hoofddoelPageUrl = '/hoofddoel';
  const hoofddoelPageUrlPattern = new RegExp('/hoofddoel(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const hoofddoelSample = {};

  let hoofddoel;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/hoofddoels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/hoofddoels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/hoofddoels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (hoofddoel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/hoofddoels/${hoofddoel.id}`,
      }).then(() => {
        hoofddoel = undefined;
      });
    }
  });

  it('Hoofddoels menu should load Hoofddoels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('hoofddoel');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Hoofddoel').should('exist');
    cy.url().should('match', hoofddoelPageUrlPattern);
  });

  describe('Hoofddoel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(hoofddoelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Hoofddoel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/hoofddoel/new$'));
        cy.getEntityCreateUpdateHeading('Hoofddoel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', hoofddoelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/hoofddoels',
          body: hoofddoelSample,
        }).then(({ body }) => {
          hoofddoel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/hoofddoels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [hoofddoel],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(hoofddoelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Hoofddoel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('hoofddoel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', hoofddoelPageUrlPattern);
      });

      it('edit button click should load edit Hoofddoel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Hoofddoel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', hoofddoelPageUrlPattern);
      });

      it('edit button click should load edit Hoofddoel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Hoofddoel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', hoofddoelPageUrlPattern);
      });

      it('last delete button click should delete instance of Hoofddoel', () => {
        cy.intercept('GET', '/api/hoofddoels/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('hoofddoel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', hoofddoelPageUrlPattern);

        hoofddoel = undefined;
      });
    });
  });

  describe('new Hoofddoel page', () => {
    beforeEach(() => {
      cy.visit(`${hoofddoelPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Hoofddoel');
    });

    it('should create an instance of Hoofddoel', () => {
      cy.get(`[data-cy="begindatum"]`).type('2024-04-29');
      cy.get(`[data-cy="begindatum"]`).blur();
      cy.get(`[data-cy="begindatum"]`).should('have.value', '2024-04-29');

      cy.get(`[data-cy="naam"]`).type('incidentally wage');
      cy.get(`[data-cy="naam"]`).should('have.value', 'incidentally wage');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        hoofddoel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', hoofddoelPageUrlPattern);
    });
  });
});
