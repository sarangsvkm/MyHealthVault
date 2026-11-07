# ABDM / ABHA integration

The backend includes an explicit adapter boundary and status endpoint. It deliberately does not call invented endpoints.

## Before implementation
1. Register the organization/application through the official ABDM developer process.
2. Obtain current sandbox API specifications, credentials and callback requirements.
3. Determine the correct role and permitted workflow (for example, HIU) for the product.
4. Implement the currently documented authentication, gateway, consent and health-information exchange flows.
5. Validate callback signatures/tokens according to the official specification.
6. Store consent artefacts and expiry/revocation state.
7. Complete sandbox conformance/security requirements and obtain production approval.

## Environment variables
- ABDM_MODE=disabled until configured
- ABDM_BASE_URL
- ABDM_CLIENT_ID
- ABDM_CLIENT_SECRET
- ABDM_CALLBACK_BASE_URL

Do not put credentials in the mobile app. Do not store ABHA identifiers unless required and lawfully consented. Do not assume an ABHA number itself grants access to medical records.
