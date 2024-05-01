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

describe('Subdoel e2e test', () => {
  const subdoelPageUrl = '/subdoel';
  const subdoelPageUrlPattern = new RegExp('/subdoel(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const subdoelSample = {};

  let subdoel;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/subdoels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/subdoels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/subdoels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (subdoel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/subdoels/${subdoel.id}`,
      }).then(() => {
        subdoel = undefined;
      });
    }
  });

  it('Subdoels menu should load Subdoels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('subdoel');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Subdoel').should('exist');
    cy.url().should('match', subdoelPageUrlPattern);
  });

  describe('Subdoel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subdoelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Subdoel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/subdoel/new$'));
        cy.getEntityCreateUpdateHeading('Subdoel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subdoelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/subdoels',
          body: subdoelSample,
        }).then(({ body }) => {
          subdoel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/subdoels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [subdoel],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(subdoelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Subdoel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subdoel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subdoelPageUrlPattern);
      });

      it('edit button click should load edit Subdoel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Subdoel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subdoelPageUrlPattern);
      });

      it('edit button click should load edit Subdoel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Subdoel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subdoelPageUrlPattern);
      });

      it('last delete button click should delete instance of Subdoel', () => {
        cy.intercept('GET', '/api/subdoels/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('subdoel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', subdoelPageUrlPattern);

        subdoel = undefined;
      });
    });
  });

  describe('new Subdoel page', () => {
    beforeEach(() => {
      cy.visit(`${subdoelPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Subdoel');
    });

    it('should create an instance of Subdoel', () => {
      cy.get(`[data-cy="code"]`).type('14751');
      cy.get(`[data-cy="code"]`).should('have.value', '14751');

      cy.get(`[data-cy="naam"]`).type('across');
      cy.get(`[data-cy="naam"]`).should('have.value', 'across');

      cy.get(`[data-cy="actief"]`).should('not.be.checked');
      cy.get(`[data-cy="actief"]`).click();
      cy.get(`[data-cy="actief"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        subdoel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', subdoelPageUrlPattern);
    });
  });
});
